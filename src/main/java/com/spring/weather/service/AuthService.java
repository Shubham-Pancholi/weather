package com.spring.weather.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.weather.dto.AuthRequest;
import com.spring.weather.dto.AuthResponse;
import com.spring.weather.entity.User;
import com.spring.weather.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional 
    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = User.builder()
                        .email(request.email())
                        .passwordHash(passwordHash)
                        .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail());

        return AuthResponse.of(token, savedUser.getEmail());
    }

    @Transactional (readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                                  .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        if (passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.of(token, user.getEmail());
    }
}