package com.cjs.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChengJianSheng
 * @date 2020-03-08
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/sayHello")
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    @GetMapping("/sayHi")
    public String sayHi(@RequestHeader("userId") String userId) {
        return userId;
    }

}
