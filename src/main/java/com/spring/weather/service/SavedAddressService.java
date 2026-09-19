package com.spring.weather.service;

import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.spring.weather.entity.SavedAddress;
import com.spring.weather.entity.User;
import com.spring.weather.repository.SavedAddressRepository;
import com.spring.weather.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class SavedAddressService {
    
    private final SavedAddressRepository savedAddressRepository;
    private final UserRepository userRepository;

    public String saveCity(String city, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                                  .orElseThrow(() -> new IllegalArgumentException("No user"));

        List<SavedAddress> savedAddresses = savedAddressRepository.findByUserId(user.getId());

        if (savedAddresses.size() == 5) {
            throw new IllegalStateException("Max saveable address reached");
        }

        SavedAddress toSave = SavedAddress.builder()
                                          .user(user)
                                          .city(city)
                                          .latitude(null)
                                          .latitude(null)
                                          .build();

        savedAddressRepository.save(toSave);
        return city;
    }

    public Pair<Double, Double> saveCoords(Double lat, Double lon, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                                  .orElseThrow(() -> new IllegalArgumentException("No user"));

        List<SavedAddress> savedAddresses = savedAddressRepository.findByUserId(user.getId());

        if (savedAddresses.size() == 5) {
            throw new IllegalStateException("Max saveable address reached");
        }

        SavedAddress toSave = SavedAddress.builder()
                                          .user(user)
                                          .city(null)
                                          .latitude(lat)
                                          .latitude(lon)
                                          .build();

        savedAddressRepository.save(toSave);

        return Pair.of(lat, lon);
    }

    public List<SavedAddress> getSavedAddresses(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                                  .orElseThrow(() -> new IllegalArgumentException("No user"));

        return savedAddressRepository.findByUserId(user.getId());
    }
}