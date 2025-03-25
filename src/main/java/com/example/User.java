package com.example;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;

public class User {
    String login;
    String password;
    Role role;
    ArrayList<Vehicle> rentedVehicles;

    public User(String login, String password, Role role, ArrayList<Vehicle> rentedVehicles) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.rentedVehicles = rentedVehicles;
    }

    public String toCsV(){
        //String hashedPassword = DigestUtils.sha256Hex(password);
        String userLine = login + ";" + password + ";" + role;

        if (rentedVehicles != null) {
            for (Vehicle vehicle : rentedVehicles) {
                userLine += ";" + vehicle.id;
            }
        }
        return userLine;
    }
}
