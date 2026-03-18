package com.jjcoffee.coffee_shop_api.controller;


import com.jjcoffee.coffee_shop_api.dto.CoffeeRequest;
import com.jjcoffee.coffee_shop_api.entity.Coffee;
import com.jjcoffee.coffee_shop_api.service.CoffeeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coffees")
public class CoffeeController {

    private final CoffeeService coffeeService;

    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @GetMapping
    public List<Coffee> getAllCoffees() {
        return coffeeService.getAllCoffees();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee createCoffee(@Valid @RequestBody CoffeeRequest coffeeRequest){
        return coffeeService.createCoffee(coffeeRequest);
    }

    @PutMapping("/{id}")
    public Coffee updateCoffee(@PathVariable Long id, @Valid @RequestBody CoffeeRequest coffeeRequest) {
        return coffeeService.updateCoffee(id, coffeeRequest);
    }

    @PatchMapping("/{id}")
    public Coffee patchCoffee(@PathVariable Long id, @Valid @RequestBody CoffeeRequest coffeeRequest) {
        return coffeeService.patchCoffee(id, coffeeRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoffee(@PathVariable Long id) {
        coffeeService.deleteCoffee(id);

        return ResponseEntity.ok("Coffee deleted successfully");
    }

}
