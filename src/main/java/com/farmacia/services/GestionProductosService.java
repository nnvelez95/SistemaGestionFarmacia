package com.farmacia.services;

import com.farmacia.database.DatabaseConnection;
import com.farmacia.models.Producto;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionProductosService {

    private Scanner scanner;

    public GestionProductosService(Scanner scanner) {
        this.scanner = scanner;
    }

    // 1. ACTUALIZAR PRECIO DE 1 PRODUCTO (SIN ID)
    public void actualizarPrecioIndividual() {
        System.out.println("\n=== ACTUALIZAR PRECIO INDIVIDUAL ===");

        System.out.print("Ingrese código de barras o nombre del producto: ");
        String busqueda = scanner.nextLine().trim();

        Producto producto = buscarProductoPorCodigoONombre(busqueda);

        if (producto == null) {
            System.out.println("❌ Producto no encontrado");
            return;
        }

        // Mostrar información actual
        System.out.println("\n📋 PRODUCTO ENCONTRADO:");
        System.out.println("   📝 Nombre: " + producto.getProducto());
        System.out.println("   📊 Código: " + producto.getCodebar());
        System.out.println("   💰 Precio actual: $" + producto.getPrecio());
        System.out.println("   💵 Costo actual: $" + producto.getCosto());
        System.out.println("   📦 Stock actual: " + producto.getCantidad());

        // Solicitar nuevo precio
        System.out.print("\n💵 Ingrese nuevo precio: $");
        try {
            BigDecimal nuevoPrecio = new BigDecimal(scanner.nextLine().trim());

            if (nuevoPrecio.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("❌ El precio no puede ser negativo");
                return;
            }

            // Confirmar
            System.out.print("¿Confirmar actualización? (s/n): ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("s")) {
                producto.setPrecio(nuevoPrecio);
                ProductoService productoService = new ProductoService();
                boolean actualizado = productoService.actualizarProducto(producto);

                if (actualizado) {
                    System.out.println("✅ Precio actualizado correctamente");
                    System.out.println("💰 Nuevo precio: $" + nuevoPrecio);
                } else {
                    System.out.println("❌ Error al actualizar precio");
                }
            } else {
                System.out.println("⚠️ Actualización cancelada");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Formato de precio inválido");
        }
    }

    // 2. ACTUALIZAR STOCK DE 1 PRODUCTO (SIN ID)
    public void actualizarStockIndividual() {
        System.out.println("\n=== ACTUALIZAR STOCK INDIVIDUAL ===");

        System.out.print("Ingrese código de barras o nombre del producto: ");
        String busqueda = scanner.nextLine().trim();

        Producto producto = buscarProductoPorCodigoONombre(busqueda);

        if (producto == null) {
            System.out.println("❌ Producto no encontrado");
            return;
        }

        System.out.println("\n📋 PRODUCTO: " + producto.getProducto());
        System.out.println("📦 Stock actual: " + producto.getCantidad());

        System.out.print("\n📦 Ingrese nuevo stock: ");
        try {
            int nuevoStock = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("¿Confirmar actualización? (s/n): ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("s")) {
                producto.setCantidad(nuevoStock);
                ProductoService productoService = new ProductoService();
                boolean actualizado = productoService.actualizarProducto(producto);

                if (actualizado) {
                    System.out.println("✅ Stock actualizado correctamente");
                    System.out.println("📦 Nuevo stock: " + nuevoStock);
                } else {
                    System.out.println("❌ Error al actualizar stock");
                }
            } else {
                System.out.println("⚠️ Actualización cancelada");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Formato de stock inválido");
        }
    }

    // 3. AGREGAR NUEVO PRODUCTO (ID AUTOGENERADO)
    public void agregarProductoIndividual() {
        System.out.println("\n=== AGREGAR NUEVO PRODUCTO ===");

        Producto nuevoProducto = new Producto(); // ID se autogenera

        try {
            // Datos básicos (sin ID)
            System.out.print("📝 Nombre del producto: ");
            String nombre = scanner.nextLine().trim();

            if (nombre.isEmpty()) {
                System.out.println("❌ El nombre no puede estar vacío");
                return;
            }

            nuevoProducto.setProducto(nombre);

            System.out.print("📊 Código de barras: ");
            String codigoBarras = scanner.nextLine().trim();

            // Verificar que no exista el código de barras
            ProductoService productoService = new ProductoService();
            if (productoService.obtenerPorCodigoBarras(codigoBarras) != null) {
                System.out.println("❌ Ya existe un producto con ese código de barras");
                return;
            }

            nuevoProducto.setCodebar(codigoBarras);

            System.out.print("💵 Precio: $");
            nuevoProducto.setPrecio(new BigDecimal(scanner.nextLine().trim()));

            System.out.print("💵 Costo: $");
            nuevoProducto.setCosto(new BigDecimal(scanner.nextLine().trim()));

            System.out.print("📦 Stock inicial: ");
            nuevoProducto.setCantidad(Integer.parseInt(scanner.nextLine().trim()));

            System.out.print("🏭 Laboratorio: ");
            nuevoProducto.setLaboratorio(scanner.nextLine().trim());

            System.out.print("💊 Principio activo: ");
            nuevoProducto.setDroga(scanner.nextLine().trim());

            System.out.print("📁 Rubro: ");
            nuevoProducto.setRubro(scanner.nextLine().trim());

            // Campos opcionales
            System.out.print("🏷️ Troquel (opcional): ");
            nuevoProducto.setTroquel(scanner.nextLine().trim());

            System.out.print("📋 Presentación (opcional): ");
            nuevoProducto.setPresentacion(scanner.nextLine().trim());

            // Mostrar resumen
            System.out.println("\n📋 RESUMEN DEL PRODUCTO:");
            System.out.println("   🆔 ID (autogenerado): " + nuevoProducto.getIdproducto());
            System.out.println("   📝 Nombre: " + nuevoProducto.getProducto());
            System.out.println("   📊 Código: " + nuevoProducto.getCodebar());
            System.out.println("   💰 Precio: $" + nuevoProducto.getPrecio());
            System.out.println("   📦 Stock: " + nuevoProducto.getCantidad());
            System.out.println("   🏭 Laboratorio: " + nuevoProducto.getLaboratorio());

            System.out.print("\n¿Confirmar creación? (s/n): ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("s")) {
                boolean creado = productoService.insertarProducto(nuevoProducto);
                if (creado) {
                    System.out.println("✅ Producto agregado correctamente");
                    System.out.println("🆔 ID asignado: " + nuevoProducto.getIdproducto());
                } else {
                    System.out.println("❌ Error al agregar producto");
                }
            } else {
                System.out.println("⚠️ Creación cancelada");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error en formato numérico");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // 4. BUSCAR PRODUCTO AVANZADO
    public void buscarProductoAvanzado() {
        System.out.println("\n=== BÚSQUEDA AVANZADA ===");
        System.out.println("1. Buscar por nombre");
        System.out.println("2. Buscar por principio activo");
        System.out.println("3. Buscar por laboratorio");
        System.out.println("4. Buscar por rubro");
        System.out.print("Seleccione opción: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            System.out.print("Ingrese término de búsqueda: ");
            String termino = scanner.nextLine().trim();

            ProductoService productoService = new ProductoService();
            java.util.List<Producto> resultados = new java.util.ArrayList<>();

            switch (opcion) {
                case 1:
                    resultados = buscarPorNombre(termino);
                    break;
                case 2:
                    resultados = buscarPorDroga(termino);
                    break;
                case 3:
                    resultados = buscarPorLaboratorio(termino);
                    break;
                case 4:
                    resultados = buscarPorRubro(termino);
                    break;
                default:
                    System.out.println("❌ Opción inválida");
                    return;
            }

            System.out.println("\n🔍 RESULTADOS DE BÚSQUEDA:");
            System.out.println("📊 Encontrados: " + resultados.size() + " productos");

            int limite = Math.min(10, resultados.size());
            for (int i = 0; i < limite; i++) {
                Producto p = resultados.get(i);
                System.out.println("\n" + (i + 1) + ". " + p.getProducto());
                System.out.println("   🆔 ID: " + p.getIdproducto() + " | 📊 Código: " + p.getCodebar());
                System.out.println("   💰 Precio: $" + p.getPrecio() + " | 📦 Stock: " + p.getCantidad());
                System.out.println("   🏭 Laboratorio: " + p.getLaboratorio() + " | 💊 Droga: " + p.getDroga());
            }

            if (resultados.size() > 10) {
                System.out.println("\nℹ️ Mostrando primeros 10 resultados de " + resultados.size());
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Opción inválida");
        }
    }

    // Métodos de búsqueda
    private java.util.List<Producto> buscarPorNombre(String termino) {
        String sql = "SELECT * FROM productos WHERE producto LIKE ? LIMIT 50";
        return buscarConSQL(sql, "%" + termino + "%");
    }

    private java.util.List<Producto> buscarPorDroga(String termino) {
        String sql = "SELECT * FROM productos WHERE droga LIKE ? LIMIT 50";
        return buscarConSQL(sql, "%" + termino + "%");
    }

    private java.util.List<Producto> buscarPorLaboratorio(String termino) {
        String sql = "SELECT * FROM productos WHERE laboratorio LIKE ? LIMIT 50";
        return buscarConSQL(sql, "%" + termino + "%");
    }

    private java.util.List<Producto> buscarPorRubro(String termino) {
        String sql = "SELECT * FROM productos WHERE rubro LIKE ? LIMIT 50";
        return buscarConSQL(sql, "%" + termino + "%");
    }

    private java.util.List<Producto> buscarConSQL(String sql, String parametro) {
        java.util.List<Producto> resultados = new java.util.ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parametro);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdproducto(rs.getString("idproducto"));
                producto.setProducto(rs.getString("producto"));
                producto.setCodebar(rs.getString("codebar"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setCantidad(rs.getInt("cantidad"));
                producto.setLaboratorio(rs.getString("laboratorio"));
                producto.setDroga(rs.getString("droga"));
                resultados.add(producto);
            }

        } catch (SQLException e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }

        return resultados;
    }
    // 5. VER INFORMACIÓN COMPLETA (SIN ID)
    public void verInformacionCompleta() {
        System.out.println("\n=== INFORMACIÓN COMPLETA DE PRODUCTO ===");

        System.out.print("Ingrese código de barras o nombre del producto: ");
        String busqueda = scanner.nextLine().trim();

        Producto producto = buscarProductoPorCodigoONombre(busqueda);

        if (producto == null) {
            System.out.println("❌ Producto no encontrado");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 INFORMACIÓN COMPLETA DEL PRODUCTO");
        System.out.println("=".repeat(60));
        System.out.println("🆔 ID: " + producto.getIdproducto());
        System.out.println("📝 PRODUCTO: " + producto.getProducto());
        System.out.println("📊 CÓDIGO BARRAS: " + producto.getCodebar());
        System.out.println("📊 CÓDIGOS ALT.: " + producto.getCodebars());
        System.out.println("🏷️  TROQUEL: " + producto.getTroquel());
        System.out.println("📦 PRESENTACIÓN: " + producto.getPresentacion());
        System.out.println("-".repeat(60));
        System.out.println("💰 PRECIO VENTA: $" + producto.getPrecio());
        System.out.println("💵 PRECIO COSTO: $" + producto.getCosto());
        System.out.println("📦 STOCK ACTUAL: " + producto.getCantidad() + " unidades");
        System.out.println("-".repeat(60));
        System.out.println("🏭 LABORATORIO: " + producto.getLaboratorio());
        System.out.println("💊 PRINCIPIO ACTIVO: " + producto.getDroga());
        System.out.println("📁 RUBRO: " + producto.getRubro());
        System.out.println("🔤 RUBRO LETRA: " + producto.getRubroLetra());
        System.out.println("-".repeat(60));
        System.out.println("📊 IVA: " + producto.getAlicuotaiva() + "%");
        System.out.println("🏥 PRECIO PAMI: $" + producto.getPreciopami());
        System.out.println("=".repeat(60));
    }

    // 6. ELIMINAR PRODUCTO (SIN ID)
    public void eliminarProducto() {
        System.out.println("\n=== ELIMINAR PRODUCTO ===");
        System.out.println("⚠️  ADVERTENCIA: Esta acción no se puede deshacer");

        System.out.print("Ingrese código de barras o nombre del producto a eliminar: ");
        String busqueda = scanner.nextLine().trim();

        Producto producto = buscarProductoPorCodigoONombre(busqueda);

        if (producto == null) {
            System.out.println("❌ Producto no encontrado");
            return;
        }

        // Mostrar información del producto a eliminar
        System.out.println("\n📋 PRODUCTO A ELIMINAR:");
        System.out.println("📝 Nombre: " + producto.getProducto());
        System.out.println("📊 Código barras: " + producto.getCodebar());
        System.out.println("💰 Precio: $" + producto.getPrecio());
        System.out.println("📦 Stock: " + producto.getCantidad() + " unidades");
        System.out.println("🏭 Laboratorio: " + producto.getLaboratorio());

        // Verificar si tiene stock
        if (producto.getCantidad() > 0) {
            System.out.println("\n⚠️  ADVERTENCIA: Este producto tiene stock disponible");
            System.out.println("   Stock actual: " + producto.getCantidad() + " unidades");
        }

        // Confirmación múltiple por seguridad
        System.out.print("\n¿Está SEGURO de que desea ELIMINAR este producto? (si/NO): ");
        String confirmacion1 = scanner.nextLine().trim();

        if (!confirmacion1.equalsIgnoreCase("si")) {
            System.out.println("✅ Eliminación cancelada");
            return;
        }

        System.out.print("Escriba 'ELIMINAR' para confirmar: ");
        String confirmacion2 = scanner.nextLine().trim();

        if (!confirmacion2.equalsIgnoreCase("ELIMINAR")) {
            System.out.println("✅ Eliminación cancelada");
            return;
        }

        // Ejecutar eliminación
        ProductoService productoService = new ProductoService();
        boolean eliminado = productoService.eliminarProducto(producto.getIdproducto());

        if (eliminado) {
            System.out.println("✅ Producto eliminado correctamente");
            System.out.println("🗑️  Eliminado: " + producto.getProducto());
        } else {
            System.out.println("❌ Error al eliminar el producto");
        }
    }


    // 7. ELIMINAR POR LOTE (para múltiples productos)
    public void eliminarProductosPorLote() {
        System.out.println("\n=== ELIMINACIÓN POR LOTE ===");
        System.out.println("🚨 ADVERTENCIA: Operación avanzada - Use con cuidado");

        System.out.println("\nOpciones de eliminación por lote:");
        System.out.println("1. Eliminar productos sin stock");
        System.out.println("2. Eliminar productos por laboratorio");
        System.out.println("3. Eliminar productos por rubro");
        System.out.print("Seleccione opción: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    eliminarProductosSinStock();
                    break;
                case 2:
                    eliminarPorLaboratorio();
                    break;
                case 3:
                    eliminarPorRubro();
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Opción inválida");
        }
    }

    // Métodos auxiliares para eliminación por lote
    private void eliminarProductosSinStock() {
        System.out.println("\n=== ELIMINAR PRODUCTOS SIN STOCK ===");

        try (var conn = com.farmacia.database.DatabaseConnection.getConnection()) {
            // Primero contar cuántos productos se eliminarán
            String countSql = "SELECT COUNT(*) as total FROM productos WHERE cantidad = 0";
            var countStmt = conn.prepareStatement(countSql);
            var rs = countStmt.executeQuery();

            int totalAEliminar = 0;
            if (rs.next()) {
                totalAEliminar = rs.getInt("total");
            }

            if (totalAEliminar == 0) {
                System.out.println("✅ No hay productos sin stock para eliminar");
                return;
            }

            System.out.println("📊 Productos sin stock encontrados: " + totalAEliminar);
            System.out.print("¿Confirmar eliminación? (si/NO): ");
            String confirmacion = scanner.nextLine();

            if (!confirmacion.equalsIgnoreCase("si")) {
                System.out.println("✅ Eliminación cancelada");
                return;
            }

            // Ejecutar eliminación
            String deleteSql = "DELETE FROM productos WHERE cantidad = 0";
            var deleteStmt = conn.prepareStatement(deleteSql);
            int eliminados = deleteStmt.executeUpdate();

            System.out.println("✅ Eliminados: " + eliminados + " productos sin stock");

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar productos sin stock: " + e.getMessage());
        }
    }

    private void eliminarPorLaboratorio() {
        System.out.print("Ingrese el nombre del laboratorio: ");
        String laboratorio = scanner.nextLine().trim();

        System.out.println("🚨 Eliminará TODOS los productos del laboratorio: " + laboratorio);
        System.out.print("¿Está SEGURO? (si/NO): ");

        String confirmacion = scanner.nextLine();
        if (!confirmacion.equalsIgnoreCase("si")) {
            System.out.println("✅ Eliminación cancelada");
            return;
        }

        // Implementación similar a eliminarProductosSinStock()
        // ... (código para eliminar por laboratorio)
    }

    private void eliminarPorRubro() {
        System.out.print("Ingrese el rubro: ");
        String rubro = scanner.nextLine().trim();

        System.out.println("🚨 Eliminará TODOS los productos del rubro: " + rubro);
        System.out.print("¿Está SEGURO? (si/NO): ");

        String confirmacion = scanner.nextLine();
        if (!confirmacion.equalsIgnoreCase("si")) {
            System.out.println("✅ Eliminación cancelada");
            return;
        }

        // Implementación similar
    }
    // MÉTODO AUXILIAR PARA BUSCAR POR CÓDIGO O NOMBRE
    private Producto buscarProductoPorCodigoONombre(String busqueda) {
        ProductoService productoService = new ProductoService();

        // Primero intentar por código de barras
        Producto producto = productoService.obtenerPorCodigoBarras(busqueda);

        // Si no encontró por código, buscar por nombre
        if (producto == null) {
            List<Producto> resultados = buscarProductosPorNombreExacto(busqueda);
            if (resultados.size() == 1) {
                producto = resultados.get(0);
            } else if (resultados.size() > 1) {
                // Mostrar opciones si hay múltiples resultados
                producto = seleccionarProductoDeLista(resultados, "Se encontraron múltiples productos. Seleccione:");
            }
        }

        return producto;
    }

    // MÉTODO PARA BÚSQUEDA EXACTA O PARCIAL
    private List<Producto> buscarProductosPorNombreExacto(String nombre) {
        List<Producto> resultados = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE producto = ? OR producto LIKE ? LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdproducto(rs.getString("idproducto"));
                producto.setProducto(rs.getString("producto"));
                producto.setCodebar(rs.getString("codebar"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setCantidad(rs.getInt("cantidad"));
                producto.setLaboratorio(rs.getString("laboratorio"));
                resultados.add(producto);
            }

        } catch (SQLException e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }

        return resultados;
    }

    // MÉTODO PARA SELECCIONAR DE LISTA (cuando hay múltiples resultados)
    private Producto seleccionarProductoDeLista(List<Producto> productos, String mensaje) {
        System.out.println("\n" + mensaje);
        System.out.println("-".repeat(80));

        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            System.out.printf("%2d. %-50s | Stock: %3d | Precio: $%8.2f%n",
                    i + 1, p.getProducto(), p.getCantidad(), p.getPrecio());
        }

        System.out.print("\nSeleccione el producto (1-" + productos.size() + ") o 0 para cancelar: ");

        try {
            int seleccion = Integer.parseInt(scanner.nextLine());

            if (seleccion == 0) {
                System.out.println("✅ Búsqueda cancelada");
                return null;
            }

            if (seleccion < 1 || seleccion > productos.size()) {
                System.out.println("❌ Selección inválida");
                return null;
            }

            return productos.get(seleccion - 1);

        } catch (NumberFormatException e) {
            System.out.println("❌ Ingrese un número válido");
            return null;
        }
    }
}