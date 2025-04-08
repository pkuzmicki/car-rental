package com.umcsuser.carrent.app;

import com.umcsuser.carrent.db.JdbcConnectionManager;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.umcsuser.carrent.repositories.impl.jdbc.RentalJdbcRepository;
import com.umcsuser.carrent.repositories.impl.jdbc.UserJdbcRepository;
import com.umcsuser.carrent.repositories.impl.jdbc.VehicleJdbcRepository;
import com.umcsuser.carrent.repositories.impl.json.RentalJsonRepository;
import com.umcsuser.carrent.repositories.impl.json.UserJsonRepository;
import com.umcsuser.carrent.repositories.impl.json.VehicleJsonRepository;
import com.umcsuser.carrent.services.AuthService;
import com.umcsuser.carrent.services.RentalService;
import com.umcsuser.carrent.services.VehicleService;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        String storageType = "json";

        //TODO: Zmiana typu storage w zaleznosci od parametru przekazanego do programu
        //TODO: Utworzenie RentalJdbcRepository implementujacej RentalRepository
        //TODO: Utworzenie UserJdbcRepository implementujacej UserRepository

        //TODO: Dorzucenie do projektu swoich jsonrepo.

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        RentalRepository rentalRepo;

        switch (storageType) {
            case "jdbc" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();
            }
            case "json" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
        //TODO:Przerzucenie logiki wykorzystującej repozytoria do serwisów
        AuthService authService = new AuthService(userRepo);
        //TODO:W VehicleService mozna wykorzystac rentalRepo dla wyszukania dostepnych pojazdow
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(rentalRepo);

        //TODO:Przerzucenie logiki interakcji z userem do App
        App app = new App(authService, vehicleService, rentalService);
        app.run();

    }
}