package com.jjcoffee.coffee_shop_api.service.impl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjcoffee.coffee_shop_api.service.PricingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PricingServiceImpl implements PricingService {

    // Logger for debugging and monitoring
    private static final Logger log = LoggerFactory.getLogger(PricingServiceImpl.class);

    // WebClient for making HTTP calls to the external pricing API
    private final WebClient webClient;

    // ObjectMapper for parsing JSON responses from the pricing API
    private final ObjectMapper objectMapper;

    // Constructor injection for WebClient and ObjectMapper, with base URL for the pricing API injected from application properties
    public PricingServiceImpl(
            WebClient.Builder builder,
            ObjectMapper objectMapper,
            // Inject the base URL for the pricing API from application properties
            @Value("${pricing.base-url}") String baseUrl
        ) {
            this.webClient = builder
                    .baseUrl(baseUrl)
                    .build();
            this.objectMapper = objectMapper;
    }

    // public PricingServiceImpl(WebClient.Builder builder, ObjectMapper objectMapper) {
    //     this.webClient = builder
    //             .baseUrl("http://localhost:3000") // Mockoon URL
    //             .build();
    //     this.objectMapper = objectMapper;
    // }

    
    @Override
    public Optional<BigDecimal> getPriceForCoffee(Long coffeeId) {
        
        // Call the external pricing API to get the price for the given coffeeId
        try {
            // Log the API call
            String response = webClient
                    .get()
                    .uri("/pricing/{coffeeId}", coffeeId)
                    .header("Accept", "application/json")
                    .header("Correlation-Id", UUID.randomUUID().toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Log the raw response for debugging
            if (response == null) {
                log.warn("Pricing API returned empty response for coffeeId {}", coffeeId);
                return Optional.empty();
            }

            // Parse the JSON response to extract the price
            JsonNode json = objectMapper.readTree(response);
            JsonNode basePriceNode = json.get("basePrice");

            if (basePriceNode == null || basePriceNode.isNull()) {
                log.warn("basePrice not found in pricing API response for coffeeId {}", coffeeId);
                return Optional.empty();
            }

            // Convert the basePrice to BigDecimal and return it
            BigDecimal price = basePriceNode.decimalValue();

            return Optional.of(price);
        
        // Handle any exceptions that may occur during the API call or JSON parsing
        } catch (Exception e) {

                log.error("Failed to call Pricing API for coffeeId {}", coffeeId, e);
                return Optional.empty();
            }
    }
}
