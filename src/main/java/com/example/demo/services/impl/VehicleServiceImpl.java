package com.example.demo.services.impl;

import com.example.demo.model.Vehicle;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.services.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
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
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
            vehicle.setActive(true);
        }
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        Set<String> rentedVehicleIds = rentalRepository.findAll().stream()
                .map(rental -> rental.getVehicle().getId())
                .collect(Collectors.toSet());
        return vehicleRepository.findByIsActiveTrueAndIdNotIn(rentedVehicleIds);
    }

    @Override
    public List<Vehicle> findRentedVehicles() {
        return rentalRepository.findAll().stream()
                .map(rental -> rental.getVehicle())
                .filter(Vehicle::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findByIdAndIsActiveTrue(vehicleId);
        if (vehicle.isEmpty()) return false;

//        return rentalRepository.findAll().stream()
//                .noneMatch(rental -> rental.getVehicle().getId().equals(vehicleId));
        return rentalRepository.findAll().stream()
                .noneMatch(rental ->
                        rental.getVehicle().getId().equals(vehicleId) &&
                                rental.getReturnDate() == null);
    }

    @Override
    public void deleteById(String id) {
        vehicleRepository.deleteById(id);
    }

    @Override
    public boolean softDeleteById(String id) {
        //System.out.println("aafafafaagaafafafaagaafafafaagaafafafaagaafafafaagaafafafaagaafafafaagaafafafaagaafafafaagaafafafaagaafafafaagaafafafaagaafafafaagaafafafaag");
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            vehicle.setActive(false);
            vehicleRepository.save(vehicle);
            return true;
        }
        return false;
    }
}
