package com.example.demo.controller;

import com.example.demo.dto.RentalRequest;
import com.example.demo.model.Rental;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final UserRepository userRepository;

    public RentalController(RentalService rentalService, UserRepository userRepository) {
        this.rentalService = rentalService;
        this.userRepository = userRepository;
    }

//    @PostMapping("/rent")
//    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest) {
//        if (rentalRequest.vehicleId == null || rentalRequest.userId == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        try {
//            Rental rental = rentalService.rent(rentalRequest.vehicleId, rentalRequest.userId);
//            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @PostMapping("/rent")
    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + login));

        Rental rental = rentalService.rent(rentalRequest.getVehicleId(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }


    @PostMapping("/return")
    public ResponseEntity<Void> returnVehicle(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + login));

        boolean returned = rentalService.returnRental(rentalRequest.getVehicleId(), user.getId());

        if (returned) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
