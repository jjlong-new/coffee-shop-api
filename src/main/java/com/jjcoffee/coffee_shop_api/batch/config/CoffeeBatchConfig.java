package com.jjcoffee.coffee_shop_api.batch.config;

import com.jjcoffee.coffee_shop_api.batch.dto.*;
import com.jjcoffee.coffee_shop_api.batch.processor.CoffeeOrderProcessor;
import com.jjcoffee.coffee_shop_api.batch.listener.JobCompletionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParametersValidator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcPagingItemReader;
import org.springframework.batch.infrastructure.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CoffeeBatchConfig {

    private final DataSource dataSource;

    // ================= VALIDATOR =================
    @Bean
    public JobParametersValidator validator() {
        return params -> {
            if (params.getString("startDate") == null ||
                params.getString("endDate") == null ||
                params.getString("outputFilePath") == null ||
                params.getLong("storeId") == null) {
                throw new InvalidJobParametersException("Missing required parameters");
            }
        };
    }

    // ================= READER =================
    @Bean
    @StepScope
    public JdbcPagingItemReader<OrderBatchDto> reader(
            @Value("#{jobParameters['storeId']}") Long storeId,
            @Value("#{T(java.time.LocalDate).parse(jobParameters['startDate'])}") LocalDate startDate,
            @Value("#{T(java.time.LocalDate).parse(jobParameters['endDate'])}") LocalDate endDate) throws Exception {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM coffee_orders");
        queryProvider.setWhereClause(
                "WHERE store_id = :storeId AND order_timestamp BETWEEN :startDate AND :endDate"
        );
        queryProvider.setSortKeys(Map.of("order_id", Order.ASCENDING));

        Map<String, Object> params = new HashMap<>();
        params.put("storeId", storeId);
        params.put("startDate", startDateTime);
        params.put("endDate", endDateTime);

        return new JdbcPagingItemReaderBuilder<OrderBatchDto>()
                .name("orderReader")
                .dataSource(dataSource)
                .pageSize(100)
                .rowMapper(new BeanPropertyRowMapper<>(OrderBatchDto.class))
                .queryProvider(queryProvider)
                .parameterValues(params)
                .saveState(true)
                .build();
    }

    // ================= WRITER =================
    @Bean
    @StepScope
    public FlatFileItemWriter<OrderCsvDto> writer(
            @Value("#{jobParameters['outputFilePath']}") String path) {

        // Field Extractor
        BeanWrapperFieldExtractor<OrderCsvDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
            fieldExtractor.setNames(new String[]{
                    "orderId","storeId","customerId","beverageName","size",
                    "quantity","unitPrice","totalPrice","discountApplied","orderTimestamp"
            });

        // Line Aggregator
        DelimitedLineAggregator<OrderCsvDto> lineAggregator = new DelimitedLineAggregator<>();
            lineAggregator.setDelimiter(",");
            lineAggregator.setFieldExtractor(fieldExtractor);

        // Writer
        return new FlatFileItemWriterBuilder<OrderCsvDto>()
                .name("orderWriter")
                .resource(new FileSystemResource(path))
                .headerCallback(writer -> writer.write(
                        "orderId,storeId,customerId,beverageName,size,quantity,unitPrice,totalPrice,discountApplied,orderTimestamp"
                ))
                .lineAggregator(lineAggregator)
                .build();
    }
    /*     public FlatFileItemWriter<OrderCsvDto> writer(
                @Value("#{jobParameters['outputFilePath']}") String path) {

            return new FlatFileItemWriterBuilder<OrderCsvDto>()
                    .name("orderWriter")
                    .resource(new FileSystemResource(path))
                    .headerCallback(w -> w.write(
                            "orderId,storeId,customerId,beverageName,size,quantity,unitPrice,totalPrice,discountApplied,orderTimestamp"
                    ))
                    .lineAggregator(new DelimitedLineAggregator<OrderCsvDto>() {{
                        setDelimiter(",");
                        setFieldExtractor(new BeanWrapperFieldExtractor<OrderCsvDto>() {{
                            setNames(new String[]{
                                    "orderId","storeId","customerId","beverageName","size",
                                    "quantity","unitPrice","totalPrice","discountApplied","orderTimestamp"
                            });
                        }});
                    }})
                    .build();
        } */

    // ================= STEP =================
    @Bean
    public Step step(JobRepository jobRepository,
                    PlatformTransactionManager transactionManager,
                    JdbcPagingItemReader<OrderBatchDto> reader,
                    CoffeeOrderProcessor processor,
                    FlatFileItemWriter<OrderCsvDto> writer) {

        return new StepBuilder("coffeeSalesStep", jobRepository)
                .<OrderBatchDto, OrderCsvDto>chunk(100)
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(100)
                .build();
    }

    // ================= JOB =================
    @Bean
    public Job job(JobRepository jobRepository,
                Step step,
                JobCompletionListener listener) {

        return new JobBuilder("coffeeSalesExportJob", jobRepository)
                .validator(validator())
                .listener(listener)
                .start(step)
                .build();
    }
}
