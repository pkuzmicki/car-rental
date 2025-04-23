package com.umcsuser.carrent.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rental")
public class Rental {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "vehicle_id", nullable = false)
    @Column(name = "vehicle_id", nullable = false)
    //private Vehicle vehicle;
    private String vehicleId;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "user_id", nullable = false)
    @Column(name = "user_id", nullable = false)
    //private User user;
    private String userId;

    @Column(name = "rent_date")
    private String rentDate;

    @Column(name = "return_date")
    private String returnDate;

}