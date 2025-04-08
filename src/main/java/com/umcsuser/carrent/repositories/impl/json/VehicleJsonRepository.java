package com.umcsuser.carrent.repositories.impl.json;

import com.umcsuser.carrent.db.JsonFileStorage;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.util.*;

@Getter
public class VehicleJsonRepository implements VehicleRepository {

    private final JsonFileStorage<Vehicle> storage;
    private final List<Vehicle> vehicles;

    public VehicleJsonRepository() {
        this.storage = new JsonFileStorage<>("vehicles.json", new TypeToken<List<Vehicle>>(){}.getType());
        this.vehicles = new ArrayList<>(storage.load());
    }

    @Override
    public void addVehicle(String id, String category, String brand, String model, int year, String plate, int price, Map<String, Object> atributes) {
        Vehicle vehicle = new Vehicle(id, category, brand, model, year, plate, price, atributes);
        vehicles.add(vehicle);
    }

    @Override
    public void removeVehicle(String id) {
        ArrayList<Vehicle> vehiclesToRemove = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (id.equals(vehicle.getId())) {
                vehiclesToRemove.add(vehicle);
            }
        }
        vehicles.removeAll(vehiclesToRemove);
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicles.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    @Override
    public void deleteById(String id) {
        vehicles.removeIf(v -> v.getId().equals(id));
        storage.save(vehicles);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
        } else {
            deleteById(vehicle.getId());
        }
        vehicles.add(vehicle);
        storage.save(vehicles);
        return vehicle;
    }
}
