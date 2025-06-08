package com.example.demo.services.impl;

import com.example.demo.model.Rental;
import com.example.demo.model.User;
import com.example.demo.model.Vehicle;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.services.RentalService;
import com.example.demo.services.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalServiceImpl implements RentalService {
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public RentalServiceImpl(VehicleService vehicleService, VehicleRepository vehicleRepository, RentalRepository rentalRepository, UserRepository userRepository) {
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalRepository.existsByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    public Optional<Rental> findActiveRentalByVehicleId(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        if (!vehicleService.isAvailable(vehicleId)) {
            throw new IllegalStateException("Vehicle " + vehicleId + " is not available for rent.");
        }
        Vehicle vehicle = null;
        try {
            vehicle = (Vehicle) vehicleRepository.findById(vehicleId).orElseThrow(() -> new EntityNotFoundException("Vehicle consistency error. ID: " + vehicleId));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        User user = null;
        try {
            user = (User) userRepository.findById(userId).orElseThrow(() -> {
                        return new EntityNotFoundException("User not found with ID: " + userId);
                    });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        Rental newRental = Rental.builder()
                .id(UUID.randomUUID().toString())
                .vehicle(vehicle)
                .user(user)
                .rentDate(String.valueOf(LocalDateTime.now()))
                .returnDate(null)
                .build();
        Rental savedRental = rentalRepository.save(newRental);
        return savedRental;

    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        Optional<Rental> rentalOpt = rentalRepository.findByVehicleIdAndUserIdAndReturnDateIsNull(vehicleId, userId);
        if (rentalOpt.isEmpty()) {
            return false;
        }

        Rental rental = rentalOpt.get();
        rental.setReturnDate(String.valueOf(LocalDateTime.now())); // ustaw datę zwrotu na teraz
        rentalRepository.save(rental);
        return true;
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }
}
