package com.spring.weather.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping ("/api/user")
public class UserController {

    @GetMapping 
    public ResponseEntity<String> getMethod() {
        return ResponseEntity.ok("Authenticated Get Request.");
    }
}