package org.project.autoserviceapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:autoserviceApp.db");

            // Настройки для уменьшения блокировок и улучшения производительности
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL");
                stmt.execute("PRAGMA busy_timeout=30000");
                stmt.execute("PRAGMA synchronous=NORMAL");
                stmt.execute("PRAGMA cache_size=2000");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection error!");
            e.printStackTrace();
        }
        return connection;
    }

    // Вспомогательный метод для закрытия соединения
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}