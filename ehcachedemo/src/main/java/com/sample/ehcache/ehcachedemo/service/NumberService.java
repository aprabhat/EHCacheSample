package com.sample.ehcache.ehcachedemo.service;

import com.sample.ehcache.ehcachedemo.controller.NumberController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NumberService {
    // ...
    @Cacheable(
            value = "squareCache",
            key = "#number")
    public BigDecimal square(Long number) {
        BigDecimal square = BigDecimal.valueOf(number)
                .multiply(BigDecimal.valueOf(number));
        System.out.println("square of "+ number+ " is "+ square);
        return square;
    }
}
