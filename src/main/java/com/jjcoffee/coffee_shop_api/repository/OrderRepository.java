package com.jjcoffee.coffee_shop_api.repository;

import com.jjcoffee.coffee_shop_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
