package com.umcsuser.carrent.repositories.impl.jdbc;

import com.umcsuser.carrent.db.JdbcConnectionManager;
import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.repositories.RentalRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalJdbcRepository implements RentalRepository {
    @Override
    public List<Rental> findAll() {
        List<Rental> list = new ArrayList<>();
        String sql = "SELECT * FROM rental";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery();
        ) {
            while (rs.next()) {
                Rental rental = Rental.builder()
                        .id(rs.getString("id"))
                        .vehicleId(rs.getString("vehicle_id"))
                        .userId(rs.getString("user_id"))
                        .rentDate(rs.getString("rent_date"))
                        .returnDate(rs.getString("return_date"))
                        .build();
                list.add(rental);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Rental> findById(String id) {
        String sql = "SELECT * FROM rental WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .userId(rs.getString("user_id"))
                            .rentDate(rs.getString("rent_date"))
                            .returnDate(rs.getString("return_date"))
                            .build();
                    return Optional.of(rental);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        } else {
            deleteById(rental.getId());
        }

        String sql = "INSERT INTO rental (id, vehicle_id, user_id, rent_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, rental.getId());
            statement.setString(2, rental.getVehicleId());
            statement.setString(3, rental.getUserId());
            statement.setString(4, rental.getRentDate());
            statement.setString(5, rental.getReturnDate());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rental;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM rental WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return Optional.empty();
    }
}
