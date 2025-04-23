package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VehicleService {
    VehicleRepository vehicleRepo;
    public VehicleService(VehicleRepository vehicleRepo, RentalRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    public void addVehicle(String id, String category, String brand, String model, int year, String plate, BigDecimal price, Map<String, Object> attributes) {
        Vehicle vehicle = Vehicle.builder()
                .id(id)
                .category(category)
                .brand(brand)
                .model(model)
                .year(year)
                .plate(plate)
                .price(price)
                .attributes(attributes)
                .build();
        vehicleRepo.save(vehicle);
    }

    public void removeVehicle(String vehicleId) {
        vehicleRepo.deleteById(vehicleId);
    }

    public void printAll() {
        for (Vehicle vehicle : vehicleRepo.findAll()) {
            Field[] fields = vehicle.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    System.out.print(field.get(vehicle) + " ");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printById(List<String> vehicleId) {
        for (Vehicle vehicle : vehicleRepo.findAll()) {
            for (String id : vehicleId) {
                if (vehicle.getId().equals(id)) {
                    Field[] fields = vehicle.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        try {
                            System.out.print(field.get(vehicle) + " ");
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println();
                }
            }
        }
    }
}
