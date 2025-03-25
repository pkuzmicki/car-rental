package com.example;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class UserRepository implements IUserRepository {
    FileWriter fileWriter;
    public ArrayList<User> users = new ArrayList<>();
    private VehicleRepository vehicleRepository;

    public UserRepository(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public User getUser(String username) {
        for (User user : users) {
            if (username.equals(user.login)){
                return user;
            }
        }
        return null;
    }

    @Override
    public ArrayList<User> getUsers() {
        return (ArrayList<User>) users.clone();
    }

    public void save() {
        try {
            fileWriter = new FileWriter("src/main/resources/users.csv");
            BufferedWriter writer = new BufferedWriter(fileWriter);
            for (User user : users) {
                writer.write(user.toCsV() + "\n");
            }
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        users.clear();
        try {
            //BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("src/main/resources/users.csv"))));
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/users.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                ArrayList<Vehicle> userVehicles = new ArrayList<>();
                for (int i = 3; i < data.length;  i++) {
                    for (Vehicle vehicle : vehicleRepository.getVehicles()) {
                        if (vehicle.id.equals(data[i])) {
                            userVehicles.add(vehicle);
                        }
                    }
                }
                User user = new User(data[0], data[1], Role.valueOf(data[2]), userVehicles);
                users.add(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
