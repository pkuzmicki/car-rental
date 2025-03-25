package com.example;

public interface IVehicleRepository {
    void rentVehicle(String id);
    void returnVehicle(String id);

    void addVehicle(String id, String brand, String model, int year, int price, boolean rented, String category);
    void removeVehicle(String id);
    Vehicle getVehicle(String id);

    void printVehicles();
    void load();
    void save();

}
