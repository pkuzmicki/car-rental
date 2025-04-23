package com.umcsuser.carrent.app;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.services.AuthService;
import com.umcsuser.carrent.services.RentalService;
import com.umcsuser.carrent.services.VehicleService;

import java.math.BigDecimal;
import java.util.*;

public class App {

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);

    public App(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run() {
        System.out.println("1.Zaloguj się\n2.Zarejestruj się");
        boolean loggedIn = false;
        Optional<User> currentUser = Optional.empty();
        switch (scanner.next()) {
            case "1" -> {
                ArrayList<String> data = new ArrayList<>();
                System.out.println("Login:");
                data.add(scanner.next());
                System.out.println("Haslo:");
                data.add(scanner.next());
                if (authService.login(data.get(0), data.get(1)).isPresent()) {
                    loggedIn = true;
                    currentUser = authService.login(data.get(0), data.get(1));
                }
            }
            case "2" -> {
                ArrayList<String> data = new ArrayList<>();
                System.out.println("Login:");
                data.add(scanner.next());
                System.out.println("Haslo:");
                data.add(scanner.next());
                System.out.println("Rola[ADMIN|USER]:");
                data.add(scanner.next());
                if (authService.register(data.get(0), data.get(1), data.get(2))) {
                    loggedIn = true;
                }
            }
            default -> throw new IllegalArgumentException("Unknown message");
        }

        while (loggedIn) {
            System.out.println("1.Pojazdy do wypozyczenia\n"+
                    "2.Moje pojazdy\n"+
                    "3.Wypożycz pojazd\n"+
                    "4.Zwróć pojazd\n"+
                    "5.Dodaj pojazd\n"+
                    "6.Usuń pojazd\n"+
                    "7.Sprawdz czy pojazd jest wypożyczony\n"+
                    "8.Wyjdz z programu\n");

            String message = scanner.next();
            switch (message) {
                case "1" -> vehicleService.printAll();
                case "2" -> vehicleService.printById(rentalService.findUserVehicles(currentUser.get()));
                case "3"-> {
                    System.out.print("Podaj id samochodu: ");
                    rentalService.rentVehicle(scanner.next(), currentUser.get());
                }
                case "4" -> {
                    System.out.print("Podaj id samochodu: ");
                    rentalService.returnVehicle(scanner.next(), currentUser.get());
                }
                case "5" -> {
                    if (currentUser.get().getRole().equals("ADMIN")) {
                        List<String> fields = new ArrayList<>();
                        System.out.print("Podaj id: ");
                        fields.add(scanner.next());
                        System.out.print("Podaj kategorie: ");
                        fields.add(scanner.next());
                        System.out.print("Podaj marke: ");
                        fields.add(scanner.next());
                        System.out.print("Podaj model: ");
                        fields.add(scanner.next());
                        System.out.print("Podaj rok produkcji: ");
                        fields.add(scanner.next());
                        System.out.print("Podaj rejestracje: ");
                        fields.add(scanner.next());
                        System.out.print("Podaj cene: ");
                        fields.add(scanner.next());
                        System.out.print("Podaj ilosc atrybutów: ");
                        fields.add(scanner.next());
                        Map<String, Object> att = new HashMap<>(Map.of());
                        if (Integer.parseInt(fields.get(7)) > 0) {
                            for (int i = 0; i < Integer.parseInt(fields.get(7)); i++) {
                                att.put(scanner.next(), scanner.next());
                            }
                        }
                        vehicleService.addVehicle(fields.get(0), fields.get(1), fields.get(2), fields.get(3), Integer.parseInt(fields.get(4)), fields.get(5), BigDecimal.valueOf(Double.parseDouble(fields.get(6))), att);
                    } else {
                        System.out.println("Nie masz uprawnień do wykonania tej operacji");
                    }
                }
                case "6" -> {
                    if (currentUser.get().getRole().equals("ADMIN")) {
                        System.out.print("Podaj id pojazdu: ");
                        vehicleService.removeVehicle(scanner.next());
                    } else {
                        System.out.println("Nie masz uprawnień do wykonania tej operacji");
                    }
                }
                case "7" -> {
                    System.out.print("Podaj id: ");
                    rentalService.isVehicleRented(scanner.next());
                }
                case "8" -> loggedIn = false;
            }
        }



    }
}
