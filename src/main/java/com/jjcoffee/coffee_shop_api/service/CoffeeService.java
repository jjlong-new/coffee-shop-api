package com.jjcoffee.coffee_shop_api.service;

import com.jjcoffee.coffee_shop_api.dto.CoffeeRequest;
import com.jjcoffee.coffee_shop_api.entity.Coffee;

import java.util.List;

public interface CoffeeService {

    Coffee createCoffee(CoffeeRequest coffeeRequest);

    List<Coffee> getAllCoffees();

    Coffee updateCoffee(Long coffeeId, CoffeeRequest coffeeRequest);

    void deleteCoffee(Long id);

    Coffee patchCoffee(Long id, CoffeeRequest coffeeRequest);
}
