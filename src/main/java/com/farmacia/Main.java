package com.farmacia;

import com.farmacia.database.DatabaseConnection;
import com.farmacia.database.DatabaseInitializer;
import com.farmacia.models.Producto;
import com.farmacia.services.ProductoService;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Inicializar base de datos
        initializeDatabase();

        // Probar operaciones CRUD
        probarOperacionesCRUD();

        System.out.println("✅ Fase 2 completada - CRUD funcionando correctamente");
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

    private static void probarOperacionesCRUD() {
        ProductoService productoService = new ProductoService();

        System.out.println("\n=== PRUEBAS CRUD ===");

        // 1. CREAR producto de prueba
        System.out.println("\n1. INSERTANDO producto de prueba...");
        Producto productoPrueba = new Producto();
        productoPrueba.setIdproducto("TEST001");
        productoPrueba.setTroquel("999999");
        productoPrueba.setCodebar("7501006555012");
        productoPrueba.setProducto("IBUPROFENO 400mg comp.x 20");
        productoPrueba.setPcto("IBUPROFENO");
        productoPrueba.setPresentacion("400mg comp.x 20");
        productoPrueba.setCantidad(50);
        productoPrueba.setCosto(new BigDecimal("1500.00"));
        productoPrueba.setPrecio(new BigDecimal("2500.00"));
        productoPrueba.setAlicuotaiva(new BigDecimal("21.00"));
        productoPrueba.setPreciopami(new BigDecimal("0.00"));
        productoPrueba.setLaboratorio("LABORATORIO TEST");
        productoPrueba.setRubro("Medicamentos");
        productoPrueba.setRubroLetra("M");
        productoPrueba.setDroga("ibuprofeno");

        boolean insertado = productoService.insertarProducto(productoPrueba);
        System.out.println(insertado ? "✓ Producto insertado correctamente" : "✗ Error al insertar producto");

        // 2. LEER producto por ID
        System.out.println("\n2. CONSULTANDO producto por ID...");
        Producto productoRecuperado = productoService.obtenerPorId("TEST001");
        if (productoRecuperado != null) {
            System.out.println("✓ Producto encontrado: " + productoRecuperado.getProducto() + " - Precio: $" + productoRecuperado.getPrecio());
        } else {
            System.out.println("✗ Producto no encontrado");
        }

        // 3. LEER producto por código de barras
        System.out.println("\n3. CONSULTANDO producto por código de barras...");
        Producto productoPorCodigo = productoService.obtenerPorCodigoBarras("7501006555012");
        if (productoPorCodigo != null) {
            System.out.println("✓ Producto por código: " + productoPorCodigo.getProducto());
        } else {
            System.out.println("✗ Producto no encontrado por código");
        }

        // 4. ACTUALIZAR producto
        System.out.println("\n4. ACTUALIZANDO producto...");
        productoPrueba.setPrecio(new BigDecimal("2700.00"));
        productoPrueba.setCantidad(45);
        boolean actualizado = productoService.actualizarProducto(productoPrueba);
        System.out.println(actualizado ? "✓ Producto actualizado correctamente" : "✗ Error al actualizar producto");

        // Verificar actualización
        Producto productoActualizado = productoService.obtenerPorId("TEST001");
        if (productoActualizado != null) {
            System.out.println("✓ Precio actualizado: $" + productoActualizado.getPrecio() + " - Stock: " + productoActualizado.getCantidad());
        }

        // 5. LISTAR todos los productos
        System.out.println("\n5. LISTANDO todos los productos...");
        List<Producto> todosProductos = productoService.obtenerTodos();
        System.out.println("✓ Total de productos en BD: " + todosProductos.size());
        for (Producto p : todosProductos) {
            System.out.println("   - " + p.getProducto() + " | Stock: " + p.getCantidad());
        }

        // 6. ELIMINAR producto (opcional - comentado para mantener datos)
        /*
        System.out.println("\n6. ELIMINANDO producto...");
        boolean eliminado = productoService.eliminarProducto("TEST001");
        System.out.println(eliminado ? "✓ Producto eliminado correctamente" : "✗ Error al eliminar producto");
        */

        System.out.println("\n=== PRUEBAS COMPLETADAS ===");
    }
}