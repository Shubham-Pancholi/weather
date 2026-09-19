package com.spring.weather.service;

import org.springframework.cache.CacheManager;
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
    private final CacheManager cacheManager;

    @Transactional 
    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = User.builder()
                        .email(request.email())
                        .passwordHash(passwordHash)
                        .version(0)
                        .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getVersion());
        cacheManager.getCache("password-versions").put(user.getEmail(), 0);

        return AuthResponse.of(token, savedUser.getEmail());
    }

    @Transactional (readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                                  .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getVersion());
        cacheManager.getCache("password-versions").put(user.getEmail(), user.getVersion());

        return AuthResponse.of(token, user.getEmail());
    }

    @Transactional 
    public void updatePassword(String userEmail, AuthRequest request, String token) {
        if (!userEmail.equals(request.email())) {
            throw new IllegalArgumentException("Email does not match with the account.");
        }

        User user = userRepository.findByEmail(request.email())
                                  .orElseThrow(() -> new IllegalArgumentException("No such user."));

        user.setPasswordHash(passwordEncoder.encode(request.password()));
        int currentVersion = user.getVersion();
        user.setVersion(currentVersion + 1);

        cacheManager.getCache("invalid-tokens").put(token, token);
        cacheManager.getCache("password-versions").evictIfPresent(user.getEmail());
        cacheManager.getCache("password-versions").put(user.getEmail(), currentVersion +1);
    }

    public void logout(String userEmail, String token) {
        cacheManager.getCache("invalid-tokens").put(token, token);
    }
}