package com.spring.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.weather.entity.SavedAddress;

public interface SavedAddressRepository extends JpaRepository<SavedAddress, Long> {
    //
}
