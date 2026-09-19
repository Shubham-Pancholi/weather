package com.spring.weather.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.weather.dto.AuthRequest;
import com.spring.weather.dto.AuthResponse;
import com.spring.weather.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/auth")
@RequiredArgsConstructor 
public class AuthController {
    
    private final AuthService authService;

    @PostMapping ("/register")
    public ResponseEntity<AuthResponse> register(
        @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(authService.register(request));
    }

    @PostMapping ("/login")
    public ResponseEntity<AuthResponse> login(
        @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PutMapping ("/update")
    public ResponseEntity<String> updatePassword(
        HttpServletRequest servletRequest,
        @AuthenticationPrincipal String currentUser,
        @Valid @RequestBody AuthRequest request
    ) {
        String header = servletRequest.getHeader("Authorization");
        authService.updatePassword(currentUser, request, header.substring(7));
        return ResponseEntity.ok("Password Changed; Please login with the new credentials.");
    }

    @PostMapping ("/logout")
    public ResponseEntity<String> logout(
        HttpServletRequest servletRequest,
        @AuthenticationPrincipal String currentUser
    ) {
        authService.logout(currentUser, servletRequest.getHeader("Authorization").substring(7));
        return ResponseEntity.ok("Logged out");
    }
}