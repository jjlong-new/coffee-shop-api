package com.jjcoffee.coffee_shop_api.batch.listener;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("JOB STARTED");
        log.info("Execution ID: {}", jobExecution.getId());
        log.info("Start Time: {}", jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("Job failed!");

            jobExecution.getAllFailureExceptions().forEach(ex ->
                log.error("Failure Exception: {}", ex.getMessage(), ex)
            );
        }

        log.info("JOB ENDED");
        log.info("Execution ID: {}", jobExecution.getId());
        log.info("End Time: {}", jobExecution.getEndTime());
        log.info("Status: {}", jobExecution.getStatus());

        jobExecution.getStepExecutions().forEach(step -> {
            log.info("Step Summary -> Read: {}, Write: {}, Skip: {}",
                    step.getReadCount(),
                    step.getWriteCount(),
                    step.getSkipCount());
        });

        if (jobExecution.getStartTime() != null && jobExecution.getEndTime() != null) {
            Duration duration = Duration.between(
                jobExecution.getStartTime(),
                jobExecution.getEndTime()
            );
            
            log.info("Duration: {} ms ({} seconds)", 
                duration.toMillis(), 
                duration.getSeconds()
            );
        }
    }
}
