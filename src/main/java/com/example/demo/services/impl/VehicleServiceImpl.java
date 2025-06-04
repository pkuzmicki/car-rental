package com.example.demo.services.impl;

import com.example.demo.model.Vehicle;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.services.VehicleService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findAllActive() {
        return vehicleRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return (Vehicle) vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return List.of();
    }

    @Override
    public List<Vehicle> findRentedVehicles() {
        return List.of();
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        return false;
    }

    @Override
    public void deleteById(String id) {

    }
}
