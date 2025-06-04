package com.example.demo.services.impl;

import com.example.demo.model.Rental;
import com.example.demo.services.RentalService;

import java.util.List;
import java.util.Optional;

public class RentalServiceImpl implements RentalService {
    @Override
    public boolean isVehicleRented(String vehicleId) {
        return false;
    }

    @Override
    public Optional<Rental> findActiveRentalByVehicleId(String vehicleId) {
        return Optional.empty();
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        return null;
    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        return false;
    }

    @Override
    public List<Rental> findAll() {
        return List.of();
    }
}
