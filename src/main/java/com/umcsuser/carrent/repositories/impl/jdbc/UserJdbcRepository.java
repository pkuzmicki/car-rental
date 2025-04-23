package com.umcsuser.carrent.repositories.impl.jdbc;

import com.umcsuser.carrent.db.JdbcConnectionManager;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserJdbcRepository implements UserRepository {

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery();
             ) {
            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getString("id"))
                        .login(rs.getString("login"))
                        .password(rs.getString("password"))
                        .role(rs.getString("role"))
                        .build();
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
            ) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getString("id"))
                            .login(rs.getString("login"))
                            .password(rs.getString("password"))
                            .role(rs.getString("role"))
                            .build();
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
            ) {
            statement.setString(1, login);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getString("id"))
                            .login(rs.getString("login"))
                            .password(rs.getString("password"))
                            .role(rs.getString("role"))
                            .build();
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        } else {
            deleteById(user.getId());
        }

        String sql = "INSERT INTO users (id, login, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
            ) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
            ) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
