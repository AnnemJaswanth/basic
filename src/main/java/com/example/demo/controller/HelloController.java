package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloController {
    @GetMapping("/")
    public String greet(HttpServletRequest request) {
        return "Welcome to this world"+request.getSession().getId();
    }
    @GetMapping("/hello")
    public String hi() {
        return "Hello World";
    }
    
    
}
