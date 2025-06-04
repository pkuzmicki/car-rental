package com.example.demo.controller;

import com.example.demo.model.Vehicle;
import com.example.demo.services.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleId(@PathVariable String id) {

        return vehicleService.findById(id).map(vehicle -> {
            return ResponseEntity.ok(vehicle);
        }).orElseGet(() -> {
            return ResponseEntity.notFound().build();
        });
    }

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle savedVehicle = vehicleService.save(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public Vehicle save(Vehicle vehicle) {
//        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
//            vehicle.setId(UUID.randomUUID().toString());
//            vehicle.setActive(true);
//        }
//        Vehicle savedVehicle = vehicleRepository.save(vehicle);
//    }
}
