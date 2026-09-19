package com.spring.weather.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.spring.weather.entity.User;
import com.spring.weather.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserService {
    
    private final UserRepository userRepository;

    @Cacheable (value = "password-version", key = "#email")
    public Integer getUserPasswordVersion(String email) {
        User user = userRepository.findByEmail(email)
                                  .orElseThrow(() -> new IllegalArgumentException("No user with email."));

        return user.getVersion();
    }
}