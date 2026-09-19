package com.spring.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.weather.entity.SavedAddress;

public interface SavedAddressRepository extends JpaRepository<SavedAddress, Long> {
    
    List<SavedAddress> findByUserId(Long userId);
}
