package com.jjcoffee.coffee_shop_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Bean definition for WebClient.Builder to be used across the application
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
