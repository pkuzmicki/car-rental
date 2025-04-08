package com.umcsuser.carrent.repositories;

import com.umcsuser.carrent.models.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VehicleRepository {
    void addVehicle(String id, String category, String brand, String model, int year, String plate, int price, Map<String, Object> atributes);

    void removeVehicle(String id);

    List<Vehicle> findAll();
    Optional<Vehicle> findById(String id);
    Vehicle save(Vehicle vehicle);
    void deleteById(String id);
}