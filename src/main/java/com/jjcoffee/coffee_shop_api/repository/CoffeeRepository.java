package com.jjcoffee.coffee_shop_api.repository;

import com.jjcoffee.coffee_shop_api.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

}
