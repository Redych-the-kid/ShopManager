package org.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Вспомогательный класс для соединения с базой данных Postgres
 */
public class DatabaseConnection{
    public static Connection getConnection() throws SQLException, IOException {
        Properties properties = new Properties();
        Connection connection;
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if(input == null) {
                throw new IOException();
            }
            properties.load(input);
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}
