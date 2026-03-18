package com.jjcoffee.coffee_shop_api.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface PricingService {

    public Optional<BigDecimal> getPriceForCoffee(Long coffeeId);

}
