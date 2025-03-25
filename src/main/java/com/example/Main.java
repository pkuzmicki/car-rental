package com.example;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        VehicleRepository vehicleRepository = new VehicleRepository();
        UserRepository userRepository = new UserRepository(vehicleRepository);
        Authentication authentication = new Authentication(userRepository);
        Scanner scanner = new Scanner(System.in);
        String command;
        String[] data;

        vehicleRepository.load();
        userRepository.load();

        //userRepository.users.add(new User("admin", DigestUtils.sha256Hex("root"), Role.ADMIN, null));
        //userRepository.users.add(new User("jan_kowalski", DigestUtils.sha256Hex("password123"), Role.CLIENT, new ArrayList<>()));
        //userRepository.users.add(new User("pawel_szabla", DigestUtils.sha256Hex("autobus"), Role.CLIENT, new ArrayList<>()));
        //userRepository.save();

        for (int i = 0; i < 3; i++) {
            System.out.println("Log in by typing: username password");
            command = scanner.nextLine();
            data = command.split(" ");

            if (!Arrays.asList(data).contains(null) && Arrays.asList(data).size() == 2) {
                if (authentication.authenticate(data[0], DigestUtils.sha256Hex(data[1]))) {
                    System.out.println("Successfully logged in!");
                    break;
                } else {
                    System.out.println("Enter correct username and password");

                    if (i == 2) {
                        System.out.println("Too many failed attempts.");
                        System.exit(0);
                    }
                }
            } else {
                i--;
                System.out.println("Enter correct data: username password");
            }
        }

        while (true) {
            if (authentication.getCurrentUser().role == Role.ADMIN) {
                System.out.println("Commands: vehicles - print list of all vehicles; ad id brand model year price rented category(only if motorcycle); remove id; users - prints list of all users; exit");
                command = scanner.nextLine();
                data = command.split(" ");
                switch (data[0]) {
                    case "vehicles" -> {
                        System.out.println("All vehicles: ");
                        vehicleRepository.printVehicles();
                    }
                    case "add" -> {
                        vehicleRepository.addVehicle(data[1], data[2], data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]), Boolean.parseBoolean(data[6]), data.length > 7 ? data[7] : null);
                        System.out.println("Successfully added vehicle " + data[1] + " to repository");
                    }
                    case "remove" -> {
                        vehicleRepository.removeVehicle(data[1]);
                        System.out.println("Successfully removed vehicle " + data[1] + " from repository");
                    }
                    case "users" -> {
                        for (User user : userRepository.getUsers()) {
                            System.out.println(user.login +" "+ user.role);
                            for (Vehicle vehicle: user.rentedVehicles) {
                                System.out.println(vehicle.id +" "+ vehicle.brand +" "+ vehicle.model +" "+ vehicle.year +" "+ vehicle.rented);
                            }
                        }
                    }
                    case "exit" -> {
                        vehicleRepository.save();
                        userRepository.save();
                        return;
                    }
                }
            } else if (authentication.getCurrentUser().role == Role.CLIENT) {
                System.out.println("Commands: vehicles - print list of all vehicles; rent id; return id; details - to view account details and rented vehicles ; exit");
                command = scanner.nextLine();
                data = command.split(" ");
                switch (data[0]) {
                    case "vehicles" -> {
                        System.out.println("All vehicles: ");
                        vehicleRepository.printVehicles();
                    }
                    case "rent" -> {
                        vehicleRepository.rentVehicle(data[1]);
                        authentication.getCurrentUser().rentedVehicles.add(vehicleRepository.getVehicle(data[1]));
                    }
                    case "return" -> {
                        vehicleRepository.returnVehicle(data[1]);
                        authentication.getCurrentUser().rentedVehicles.remove(vehicleRepository.getVehicle(data[1]));
                    }
                    case "details" -> {
                        System.out.println("Login: " + authentication.getCurrentUser().login);
                        System.out.println("Your vehicles: ");
                        for (Vehicle vehicle: authentication.getCurrentUser().rentedVehicles) {
                            System.out.println(vehicle.id +" "+ vehicle.brand +" "+ vehicle.model +" "+ vehicle.year +" "+ vehicle.rented);
                        }
                    }
                    case "exit" -> {
                        vehicleRepository.save();
                        userRepository.save();
                        return;
                    }
                }
                vehicleRepository.save();
                userRepository.save();
            }
        }
    }
}
