package com.farmacia.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void createTables() {
        String createProductosTable = """
            CREATE TABLE IF NOT EXISTS productos (
                idproducto VARCHAR(20) PRIMARY KEY,
                troquel VARCHAR(20),
                codebar VARCHAR(50) UNIQUE,
                codebars VARCHAR(100),
                producto VARCHAR(255) NOT NULL,
                pcto VARCHAR(100),
                presentacion VARCHAR(255),
                cantidad INTEGER DEFAULT 0,
                costo DECIMAL(10,2),
                precio DECIMAL(10,2) NOT NULL,
                fecha_ultimo_precio DATETIME,
                alicuotaiva DECIMAL(5,2) DEFAULT 0,
                preciopami DECIMAL(10,2) DEFAULT 0,
                laboratorio VARCHAR(100),
                rubro VARCHAR(50),
                rubro_letra CHAR(1),
                droga VARCHAR(255)
            )
        """;

        String createIndexes = """
            CREATE INDEX IF NOT EXISTS idx_codebar ON productos(codebar);
            CREATE INDEX IF NOT EXISTS idx_producto ON productos(producto);
            CREATE INDEX IF NOT EXISTS idx_rubro ON productos(rubro);
            CREATE INDEX IF NOT EXISTS idx_laboratorio ON productos(laboratorio);
            CREATE INDEX IF NOT EXISTS idx_droga ON productos(droga);
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Crear tabla
            stmt.execute(createProductosTable);

            // Crear índices
            stmt.execute(createIndexes);

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear las tablas: " + e.getMessage(), e);
        }
    }
}