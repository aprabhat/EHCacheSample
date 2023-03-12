package com.sample.ehcache.ehcachedemo.controller;

import com.sample.ehcache.ehcachedemo.service.NumberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberController {


    @Autowired
    private NumberService numberService;

    @GetMapping(path = "/square/{number}")
    public String getSquare(@PathVariable Long number) {
        System.out.println("call numberService to square {} "+ number);
        return String.format("{\"square\": %s}", numberService.square(number));
    }
}
