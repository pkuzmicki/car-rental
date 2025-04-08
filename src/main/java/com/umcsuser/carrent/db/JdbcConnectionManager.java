package com.umcsuser.carrent.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//TODO:ustawienie zmiennej srodowiskowej DB_URL
public class JdbcConnectionManager {
    private static JdbcConnectionManager instance;
    private final String url;

    public static JdbcConnectionManager getInstance() {
        if (instance == null) {
            instance = new JdbcConnectionManager();
        }
        return instance;
    }

    private JdbcConnectionManager() {
        url = System.getenv("jdbc:postgresql://ep-nameless-violet-a24m3osv-pooler.eu-central-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_MUu36IfVqJSK&sslmode=require");
        //url = System.getenv("DB_URL");
        if (url == null) {
            throw new RuntimeException("DB_URL not set!");
        }
    }
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Connection Failed!", e);
        }
    }
}