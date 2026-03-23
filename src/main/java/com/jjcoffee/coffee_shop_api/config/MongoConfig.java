package com.jjcoffee.coffee_shop_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    // Configure the MongoClient bean to connect to the local MongoDB instance
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    // Configure the MongoTemplate bean to interact with the "coffee_db" database
    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoClient(), "coffee_db");
    }

}
