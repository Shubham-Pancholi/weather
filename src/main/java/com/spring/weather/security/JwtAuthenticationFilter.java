package com.spring.weather.security;

import java.io.IOException;
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.weather.service.JwtService;
import com.spring.weather.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final CacheManager cacheManager;
    private final UserService userService;

    @Override 
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail;

        try {
            userEmail = jwtService.extractEmail(jwt);
        }   catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null && jwtService.isTokenValid(jwt, userEmail)) {

            Cache cacheBlacklist = cacheManager.getCache("invalid-tokens");
            Cache cachePasswordVersion = cacheManager.getCache("password-versions");

            if (cacheBlacklist == null || cacheBlacklist.get(jwt) == null) {
                int passwordVersion;
                if (cachePasswordVersion == null || cachePasswordVersion.get(userEmail, Integer.class) == null) {
                    passwordVersion = userService.getUserPasswordVersion(userEmail);
                }
                else {
                    passwordVersion = cachePasswordVersion.get(userEmail, Integer.class);
                }

                if (passwordVersion == jwtService.extractPasswordVersion(jwt)) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, null, List.of(authority));
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}