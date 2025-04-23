package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    private RentalRepository rentalRepo;
    public RentalService(RentalRepository rentalRepo) {
        this.rentalRepo = rentalRepo;
    }

    public void rentVehicle(String vehicleId, User user) {
        Rental rental = Rental.builder()
                .vehicleId(vehicleId)
                .userId(user.getId())
                .rentDate(String.valueOf(LocalDateTime.now()))
                .build();
        rentalRepo.save(rental);
    }

    public void returnVehicle(String vehicleId, User user) {
        Rental rental = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).get();
         if (rental.getUserId().equals(user.getId())) {
             rental.setReturnDate(String.valueOf(LocalDateTime.now()));
             rentalRepo.save(rental);
             System.out.println("Zwrócono pojazd.");
         }
    }

    public boolean isVehicleRented(String vehicleId) {
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    public List<String> findUserVehicles(User user) {
        List<String> vehiclesId = new ArrayList<>();
        for (Rental rental : rentalRepo.findAll()) {
            if (user.getId().equals(rental.getUserId())) {
                if (rental.getReturnDate() == null) {
                    vehiclesId.add(rental.getVehicleId());
                }
            }
        }
        return vehiclesId;
    }
}
