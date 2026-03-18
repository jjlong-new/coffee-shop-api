package com.jjcoffee.coffee_shop_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuration class to set up OpenAPI documentation for the Coffee Shop API
@Configuration
public class OpenApiConfig {
    // Define a bean that creates an OpenAPI instance with API metadata
    @Bean
    public OpenAPI coffeeShopAPI() {
        // Create and return an OpenAPI instance with API metadata
        return new OpenAPI()
                .info(new Info()
                        .title("Coffee Shop API")
                        .version("1.0")
                        .description("API for managing coffees and orders"));
    }
}
