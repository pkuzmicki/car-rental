package com.example;

import java.util.Objects;

public abstract class Vehicle {
    String id;
    String brand;
    String model;
    int year;
    int price;
    boolean rented;

    public Vehicle(String id, String brand, String model, int year, int price, boolean rented) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
    }

    public String toCsV(){
        return id + ";" + brand  + ";" + model  + ";" + year  + ";" + price + ";" + rented;
    }

    public static boolean equals(Vehicle a, Vehicle b){
        if (a.getClass() == b.getClass()) {
            return a.id.equals(b.id) && a.brand.equals(b.brand) && a.model.equals(b.model) && a.year == b.year && a.price == b.price && a.rented == b.rented;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), brand, model, year, price, rented);
    }

}
