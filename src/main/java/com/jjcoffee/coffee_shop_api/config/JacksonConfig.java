package com.jjcoffee.coffee_shop_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfig {

    // Bean definition for ObjectMapper to be used across the application
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
