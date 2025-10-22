package com.farmacia.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:farmacia.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✓ Conexión a SQLite establecida correctamente");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}