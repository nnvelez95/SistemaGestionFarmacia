package com.farmacia;

import com.farmacia.database.DatabaseConnection;
import com.farmacia.database.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {
        // Inicializar base de datos
        initializeDatabase();

        System.out.println("Sistema de Gestión Farmacéutica iniciado");
        System.out.println("Base de datos inicializada correctamente");
    }

    private static void initializeDatabase() {
        try {
            DatabaseConnection.testConnection();
            DatabaseInitializer.createTables();
            System.out.println("✓ Tablas de base de datos creadas");
        } catch (Exception e) {
            System.err.println("✗ Error al inicializar base de datos: " + e.getMessage());
        }
    }
}