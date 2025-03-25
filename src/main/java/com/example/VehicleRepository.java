package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class VehicleRepository implements IVehicleRepository {
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    FileWriter fileWriter;

    @Override
    public void rentVehicle(String id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.id.equals(id)) {
                if (vehicle.rented) {
                    System.out.println("Vehicle already rented");
                    return;
                }
                vehicle.rented = true;
                System.out.println("Successfully rented a "+ vehicle.id + " " + vehicle.brand + " " + vehicle.model);
                save();
                return;
            }
        }
        System.out.println("No car with this id");
    }

    @Override
    public void returnVehicle(String id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.id.equals(id)) {
                if (!vehicle.rented) {
                    System.out.println("Vehicle already returned");
                    return;
                }
                System.out.println("Successfully returned a "+ vehicle.id + " " + vehicle.brand + " " + vehicle.model);
                vehicle.rented = false;
                save();
                return;
            }
        }
        System.out.println("No car with this id");
    }

    @Override
    public void addVehicle(String id, String brand, String model, int year, int price, boolean rented, String category) {
        if (id.charAt(0) == 'C') {
            Car car = new Car(id, brand, model, year, price, rented);
            vehicles.add(car);
        } else if (id.charAt(0) == 'M') {
            Motorcycle motorcycle = new Motorcycle(id, brand, model, year, price, rented, category);
            vehicles.add(motorcycle);
        }
        save();
    }

    @Override
    public void removeVehicle(String id) {
        ArrayList<Vehicle> vehiclesToRemove = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (id.equals(vehicle.id)) {
                if (!vehicle.rented) {
                    vehiclesToRemove.add(vehicle);
                } else {
                    System.out.println("You can't remove rented vehicle");
                }
            }
        }
        vehicles.removeAll(vehiclesToRemove);
        save();
    }

    @Override
    public Vehicle getVehicle(String id) {
        for (Vehicle vehicle : vehicles) {
            if (id.equals(vehicle.id)) {
                return vehicle;
            }
        }
        return null;
    }

    public ArrayList<Vehicle> getVehicles() {
        return (ArrayList<Vehicle>) vehicles.clone();
    }

    @Override
    public void printVehicles() {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.id.charAt(0) == 'C') {
                Car car = (Car)vehicle;
                System.out.println(car.id +" "+ car.brand +" "+ car.model +" "+ car.year +", "+ car.price + ", is rented: "+ car.rented);
            } else if (vehicle.id.charAt(0) == 'M') {
                Motorcycle motorcycle = (Motorcycle)vehicle;
                System.out.println(motorcycle.id +" "+ motorcycle.brand +" "+ motorcycle.model +" "+ motorcycle.year +", "+ motorcycle.price + ", is rented: "+ motorcycle.rented);
            }
        }
    }

    @Override
    public void load() {
        vehicles.clear();
        try {
            //BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("src/main/resources/cars.csv"))));
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/cars.csv")) ;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] vehicle = line.split(";");
                if (vehicle[0].charAt(0) == 'C') {
                    Car car = new Car(vehicle[0], vehicle[1], vehicle[2], Integer.parseInt(vehicle[3]), Integer.parseInt(vehicle[4]), Boolean.parseBoolean(vehicle[5].trim().toLowerCase()));
                    vehicles.add(car);
                } else if (vehicle[0].charAt(0) == 'M') {
                    Motorcycle motorcycle = new Motorcycle(vehicle[0], vehicle[1], vehicle[2], Integer.parseInt(vehicle[3]), Integer.parseInt(vehicle[4]), Boolean.parseBoolean(vehicle[5].trim()), vehicle[6]);
                    vehicles.add(motorcycle);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            fileWriter = new FileWriter("src/main/resources/cars.csv");
            BufferedWriter writer = new BufferedWriter(fileWriter);
            for (Vehicle vehicle : vehicles) {
                writer.write(vehicle.toCsV() + "\n");
            }
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
