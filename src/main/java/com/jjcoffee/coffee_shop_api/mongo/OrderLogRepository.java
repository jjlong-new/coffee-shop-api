package com.jjcoffee.coffee_shop_api.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderLogRepository extends MongoRepository<OrderLog, String> {

}
