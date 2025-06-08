package com.example.demo;

import com.example.demo.controller.RentalController;
import com.example.demo.controller.VehicleController;
import com.example.demo.services.RentalService;
import com.example.demo.services.VehicleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(VehicleController vehicleController, RentalController rentalController) {
		return args -> {
			Scanner scanner = new Scanner(System.in);
			boolean running = true;

			while (running) {
				System.out.println("\n=== MENU ===");
				System.out.println("1. Pokaż wszystkie pojazdy");
				System.out.println("2. Wypożycz pojazd");
				System.out.println("3. Wyjdź");
				System.out.print("Wybierz opcję: ");

				String choice = scanner.nextLine();

				switch (choice) {
					case "1":
						vehicleController.getAllVehicles();
						break;
					case "2":
						System.out.print("Podaj ID pojazdu: ");
						String vehicleId = scanner.nextLine();
						System.out.print("Podaj ID użytkownika: ");
						String userId = scanner.nextLine();


						break;
					case "3":
						running = false;
						System.exit(0);
						break;
					default:
						System.out.println("Nieznana opcja, spróbuj ponownie.");
				}
			}

			scanner.close();
		};
	}

}
