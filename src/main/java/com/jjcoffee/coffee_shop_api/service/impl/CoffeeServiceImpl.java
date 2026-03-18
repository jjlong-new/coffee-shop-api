package com.jjcoffee.coffee_shop_api.service.impl;

import com.jjcoffee.coffee_shop_api.dto.CoffeeRequest;
import com.jjcoffee.coffee_shop_api.entity.Coffee;
import com.jjcoffee.coffee_shop_api.exception.ResourceNotFoundException;
import com.jjcoffee.coffee_shop_api.repository.CoffeeRepository;
import com.jjcoffee.coffee_shop_api.service.CoffeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoffeeServiceImpl implements CoffeeService {
    // Dependency injection of the CoffeeRepository
    private final CoffeeRepository coffeeRepository;

    // Constructor injection for the repository
    public CoffeeServiceImpl(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @Override
    public Coffee createCoffee(CoffeeRequest coffeeRequest) {
        // Create a new Coffee entity and set its properties from the CoffeeRequest DTO
        Coffee coffee = Coffee.builder()
                .name(coffeeRequest.getName())
                .price(coffeeRequest.getPrice())
                .size(coffeeRequest.getSize())
                .build();

        return coffeeRepository.save(coffee);
    }


    @Override
    public List<Coffee> getAllCoffees() {
        return coffeeRepository.findAll();
    }


    @Override
    public Coffee updateCoffee(Long coffeeId, CoffeeRequest coffeeRequest) {

        Coffee coffee = coffeeRepository.findById(coffeeId).orElseThrow(() -> new ResourceNotFoundException("Coffee not found"));

        // Update the properties of the existing Coffee entity with the values from the CoffeeRequest DTO
        coffee.setName(coffeeRequest.getName());
        coffee.setPrice(coffeeRequest.getPrice());
        coffee.setSize(coffeeRequest.getSize());

        return coffeeRepository.save(coffee);
    }


    @Override
    public void deleteCoffee(Long id) {
        Coffee coffee = coffeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coffee not found"));
        coffeeRepository.delete(coffee);
    }


    @Override
    public Coffee patchCoffee(Long id, CoffeeRequest coffeeRequest) {

        Coffee coffee = coffeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Coffee not found"));
        // Update only the non-null properties of the existing Coffee entity with the values from the CoffeeRequest DTO
        if (coffeeRequest.getName() != null) {
            coffee.setName(coffeeRequest.getName());
        }
        if (coffeeRequest.getPrice() != null) {
            coffee.setPrice(coffeeRequest.getPrice());
        }
        if (coffeeRequest.getSize() != null) {
            coffee.setSize(coffeeRequest.getSize());
        }

        return coffeeRepository.save(coffee);
    }
}
