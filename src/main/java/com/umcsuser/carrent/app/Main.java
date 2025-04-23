package com.umcsuser.carrent.app;

import com.umcsuser.carrent.db.HibernateConfig;
import com.umcsuser.carrent.db.JdbcConnectionManager;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.umcsuser.carrent.repositories.impl.hibernate.RentalHiberRepository;
import com.umcsuser.carrent.repositories.impl.hibernate.UserHiberRepository;
import com.umcsuser.carrent.repositories.impl.hibernate.VehicleHiberRepository;
import com.umcsuser.carrent.repositories.impl.jdbc.RentalJdbcRepository;
import com.umcsuser.carrent.repositories.impl.jdbc.UserJdbcRepository;
import com.umcsuser.carrent.repositories.impl.jdbc.VehicleJdbcRepository;
import com.umcsuser.carrent.repositories.impl.json.RentalJsonRepository;
import com.umcsuser.carrent.repositories.impl.json.UserJsonRepository;
import com.umcsuser.carrent.repositories.impl.json.VehicleJsonRepository;
import com.umcsuser.carrent.services.AuthService;
import com.umcsuser.carrent.services.RentalService;
import com.umcsuser.carrent.services.VehicleService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("1.jdbc\n2.json\n3.hibernate");
        Scanner scanner = new Scanner(System.in);
        String storageType = scanner.next();

        //TODO: Zmiana typu storage w zaleznosci od parametru przekazanego do programu
        //TODO: Utworzenie RentalJdbcRepository implementujacej RentalRepository
        //TODO: Utworzenie UserJdbcRepository implementujacej UserRepository

        //TODO: Dorzucenie do projektu swoich jsonrepo.

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        RentalRepository rentalRepo;

        switch (storageType) {
            case "1" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();
            }
            case "2" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();
            }
            case "3" -> {
                userRepo =  new UserHiberRepository();
                vehicleRepo = new VehicleHiberRepository();
                rentalRepo = new RentalHiberRepository();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }

        //TODO:Przerzucenie logiki wykorzystującej repozytoria do serwisów
        AuthService authService = new AuthService(userRepo);
        //TODO:W VehicleService mozna wykorzystac rentalRepo dla wyszukania dostepnych pojazdow
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(rentalRepo);

        if (storageType.equals("1")) {
            try(Connection connection = JdbcConnectionManager.getInstance().getConnection();
                Statement stmt = connection.createStatement()){
                ResultSet rs = stmt.executeQuery("SELECT NOW()");
                if (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (storageType.equals("3")) {
            SessionFactory sessionFactory = HibernateConfig.getSessionFactory();
            try (Session session = sessionFactory.openSession()) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //System.out.println(userRepo.findAll());
        //System.out.println(userRepo.findById("f4b5b79b-df2c-4e0a-89fc-464908d2e6dc"));
        //System.out.println(userRepo.findByLogin("jan"));

        //TODO:Przerzucenie logiki interakcji z userem do App
        App app = new App(authService, vehicleService, rentalService);
        app.run();
    }
}