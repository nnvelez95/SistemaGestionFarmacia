package com.farmacia;


import com.farmacia.database.DatabaseConnection;
import com.farmacia.database.DatabaseInitializer;
import com.farmacia.models.Producto;
import com.farmacia.services.RecepcionPedidosService;
import com.farmacia.services.ProductoService;
import com.farmacia.services.ExcelImportService;
import com.farmacia.services.ExcelUpdateService;
import com.farmacia.services.GestionProductosService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Inicializar base de datos
        initializeDatabase();

        // Menú principal
        mostrarMenuPrincipal();
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

    private static void mostrarMenuPrincipal() {
        Scanner scanner = new Scanner(System.in);
        GestionProductosService gestionService = new GestionProductosService(scanner);
        int opcion;

        do {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("               🏥 SISTEMA DE GESTIÓN FARMACÉUTICA");
            System.out.println("=".repeat(60));
            System.out.println("👨‍💼 GESTIÓN INDIVIDUAL DE PRODUCTOS");
            System.out.println("   1.  Buscar producto por código de barras");
            System.out.println("   2.  Búsqueda avanzada de productos");
            System.out.println("   3.  Agregar nuevo producto individual");
            System.out.println("   4.  Actualizar precio de un producto");
            System.out.println("   5.  Actualizar stock de un producto");
            System.out.println("   6.  Ver información completa de producto");
            System.out.println("   7.  Eliminar producto individual");
            System.out.println("");
            System.out.println("📊 OPERACIONES MASIVAS");
            System.out.println("   8.  Importar productos desde Excel (completo)");
            System.out.println("   9.  Actualizar precios desde Excel (rápido)");
            System.out.println("");
            System.out.println("📈 INFORMES Y CONSULTAS");
            System.out.println("   10. Listar todos los productos");
            System.out.println("   11. Ver estadísticas de la base de datos");
            System.out.println("   12. Productos con stock bajo (< 10 unidades)");
            System.out.println("   13. Productos sin stock");
            System.out.println("");
            System.out.println("🔧 HERRAMIENTAS");
            System.out.println("   14. Probar operaciones CRUD");
            System.out.println("📦 RECEPCIÓN DE PEDIDOS");
            System.out.println("   15. Recepción de pedido/mercadería");
            System.out.println("   0.  Salir del sistema");
            System.out.println("-".repeat(60));
            System.out.print("💻 Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        buscarPorCodigoBarras(scanner);
                        break;
                    case 2:
                        gestionService.buscarProductoAvanzado();
                        break;
                    case 3:
                        gestionService.agregarProductoIndividual();
                        break;
                    case 4:
                        gestionService.actualizarPrecioIndividual();
                        break;
                    case 5:
                        gestionService.actualizarStockIndividual();
                        break;
                    case 6:
                        gestionService.verInformacionCompleta();
                        break;
                    case 7:
                        gestionService.eliminarProducto();
                        break;
                    case 8:
                        probarImportacionExcel(scanner);
                        break;
                    case 9:
                        actualizarPreciosDesdeExcel(scanner);
                        break;
                    case 10:
                        listarTodosProductos();
                        break;
                    case 11:
                        mostrarEstadisticas();
                        break;
                    case 12:
                        mostrarProductosStockBajo();
                        break;
                    case 13:
                        mostrarProductosSinStock();
                        break;
                    case 14:
                        probarOperacionesCRUD();
                        break;
                    case 15:
                        RecepcionPedidosService recepcionService = new RecepcionPedidosService(scanner);
                        recepcionService.recepcionarPedido();
                        break;
                    case 0:
                        System.out.println("\n👋 ¡Gracias por usar el Sistema de Gestión Farmacéutica!");
                        System.out.println("   Hasta pronto! 🏥");
                        break;
                    default:
                        System.out.println("❌ Opción no válida. Por favor, seleccione 0-14.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Por favor, ingrese un número válido (0-14)");
                opcion = -1;
            }

        } while (opcion != 0);

        scanner.close();
    }

    // === MÉTODOS EXISTENTES ===

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

        System.out.println("\n=== PRUEBAS CRUD COMPLETADAS ===");

        // Preguntar si eliminar producto de prueba
        System.out.print("\n¿Desea eliminar el producto de prueba? (s/n): ");
        Scanner scanner = new Scanner(System.in);
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("s")) {
            boolean eliminado = productoService.eliminarProducto("TEST001");
            System.out.println(eliminado ? "✓ Producto de prueba eliminado" : "✗ Error al eliminar producto");
        }
    }

    private static void probarImportacionExcel(Scanner scanner) {
        System.out.println("\n=== IMPORTACIÓN DESDE EXCEL ===");

        String rutaExcel = "C:\\Users\\Usuario\\Documents\\LISTADO_DE_PRODUCTOS.xls";

        System.out.println("📍 Ruta del archivo: " + rutaExcel);

        // Verificar si el archivo existe
        java.io.File archivo = new java.io.File(rutaExcel);
        if (!archivo.exists()) {
            System.out.println("❌ El archivo no existe en la ruta especificada");
            System.out.println("💡 Ruta actual de trabajo: " + System.getProperty("user.dir"));
            return;
        }

        System.out.println("✅ Archivo encontrado: " + archivo.getName());
        System.out.println("📏 Tamaño: " + archivo.length() + " bytes");

        System.out.print("¿Está seguro de que desea importar? (s/n): ");
        String confirmacion = scanner.nextLine();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Importación cancelada");
            return;
        }

        System.out.println("🚀 Iniciando importación...");

        ExcelImportService importService = new ExcelImportService();
        ExcelImportService.ImportResult resultado = importService.importarProductosDesdeExcel(rutaExcel);

        System.out.println("\n📊 RESULTADO DE IMPORTACIÓN:");
        System.out.println("📁 Total de registros en Excel: " + resultado.totalRegistros);
        System.out.println("✅ Importados exitosamente: " + resultado.importadosExitosos);
        System.out.println("❌ Errores: " + resultado.errores);
        System.out.println("📈 Porcentaje de éxito: " + String.format("%.2f", resultado.getPorcentajeExito()) + "%");

        // Verificar en base de datos
        ProductoService productoService = new ProductoService();
        int totalEnBD = productoService.obtenerTodos().size();
        System.out.println("💾 Total de productos en base de datos: " + totalEnBD);
    }

    private static void actualizarPreciosDesdeExcel(Scanner scanner) {
        System.out.println("\n=== ACTUALIZACIÓN RÁPIDA DE PRECIOS ===");

        String rutaExcel = "C:\\Users\\Usuario\\Documents\\LISTADO_ACTUALIZADO.xls";

        System.out.println("📍 Ruta del archivo: " + rutaExcel);

        java.io.File archivo = new java.io.File(rutaExcel);
        if (!archivo.exists()) {
            System.out.println("❌ El archivo no existe en la ruta predeterminada");
            System.out.print("¿Desea ingresar otra ruta? (s/n): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                System.out.print("Ingrese la ruta completa: ");
                rutaExcel = scanner.nextLine();
                archivo = new java.io.File(rutaExcel);
                if (!archivo.exists()) {
                    System.out.println("❌ Archivo no encontrado. Operación cancelada.");
                    return;
                }
            } else {
                return;
            }
        }

        System.out.print("¿Confirmar actualización de precios? (s/n): ");
        String confirmacion = scanner.nextLine();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("⚠️ Actualización cancelada");
            return;
        }

        System.out.println("🚀 Iniciando actualización...");

        ExcelUpdateService updateService = new ExcelUpdateService();
        ExcelUpdateService.UpdateResult resultado = updateService.actualizarSoloPrecios(rutaExcel);

        System.out.println("\n📊 RESULTADO DE ACTUALIZACIÓN:");
        System.out.println("📁 Total de registros en Excel: " + resultado.totalRegistros);
        System.out.println("🔄 Registros procesados: " + resultado.registrosProcesados);
        System.out.println("✅ Precios actualizados: " + resultado.actualizadosExitosos);
        System.out.println("❌ Productos no encontrados: " + resultado.productosNoEncontrados);
        System.out.println("💥 Errores: " + resultado.errores);
        System.out.println("📈 Porcentaje de éxito: " + String.format("%.2f", resultado.getPorcentajeExito()) + "%");

        // Mostrar resumen rápido
        if (resultado.actualizadosExitosos > 0) {
            System.out.println("\n🎉 ¡Actualización completada!");
        }
    }

    private static void mostrarEstadisticas() {
        ProductoService productoService = new ProductoService();
        List<Producto> productos = productoService.obtenerTodos();

        System.out.println("\n=== ESTADÍSTICAS DE LA BASE DE DATOS ===");
        System.out.println("📊 Total de productos: " + productos.size());

        if (!productos.isEmpty()) {
            // Calcular estadísticas básicas
            BigDecimal precioTotal = BigDecimal.ZERO;
            int stockTotal = 0;
            int productosConStock = 0;

            for (Producto p : productos) {
                if (p.getPrecio() != null) {
                    precioTotal = precioTotal.add(p.getPrecio());
                }
                stockTotal += p.getCantidad();
                if (p.getCantidad() > 0) {
                    productosConStock++;
                }
            }

            BigDecimal precioPromedio = productos.size() > 0 ?
                    precioTotal.divide(BigDecimal.valueOf(productos.size()), 2, BigDecimal.ROUND_HALF_UP) :
                    BigDecimal.ZERO;

            System.out.println("💰 Precio promedio: $" + precioPromedio);
            System.out.println("📦 Stock total: " + stockTotal + " unidades");
            System.out.println("🏪 Productos con stock disponible: " + productosConStock);
            System.out.println("📋 Productos sin stock: " + (productos.size() - productosConStock));

            // Mostrar algunos productos de ejemplo
            System.out.println("\n📋 Primeros 5 productos:");
            int limite = Math.min(5, productos.size());
            for (int i = 0; i < limite; i++) {
                Producto p = productos.get(i);
                System.out.println("   " + (i + 1) + ". " + p.getProducto() + " - $" + p.getPrecio() + " - Stock: " + p.getCantidad());
            }
        } else {
            System.out.println("ℹ️ La base de datos está vacía");
        }
    }

    private static void buscarPorCodigoBarras(Scanner scanner) {
        System.out.println("\n=== BÚSQUEDA POR CÓDIGO DE BARRAS ===");
        System.out.print("Ingrese el código de barras: ");
        String codigoBarras = scanner.nextLine();

        if (codigoBarras.trim().isEmpty()) {
            System.out.println("❌ Debe ingresar un código de barras");
            return;
        }

        ProductoService productoService = new ProductoService();
        Producto producto = productoService.obtenerPorCodigoBarras(codigoBarras);

        if (producto != null) {
            System.out.println("\n✅ PRODUCTO ENCONTRADO:");
            System.out.println("   📝 Nombre: " + producto.getProducto());
            System.out.println("   🏷️ ID: " + producto.getIdproducto());
            System.out.println("   💰 Precio: $" + producto.getPrecio());
            System.out.println("   📦 Stock: " + producto.getCantidad());
            System.out.println("   🏭 Laboratorio: " + producto.getLaboratorio());
            System.out.println("   💊 Principio activo: " + producto.getDroga());
            System.out.println("   📁 Rubro: " + producto.getRubro());
        } else {
            System.out.println("❌ No se encontró ningún producto con ese código de barras");
        }
    }

    private static void listarTodosProductos() {
        ProductoService productoService = new ProductoService();
        List<Producto> productos = productoService.obtenerTodos();

        System.out.println("\n=== LISTADO COMPLETO DE PRODUCTOS ===");
        System.out.println("📊 Total: " + productos.size() + " productos");

        if (productos.size() > 10) {
            System.out.println("ℹ️ Mostrando primeros 10 productos...");
        }

        int limite = Math.min(10, productos.size());
        for (int i = 0; i < limite; i++) {
            Producto p = productos.get(i);
            System.out.println("\n" + (i + 1) + ". " + p.getProducto());
            System.out.println("   ID: " + p.getIdproducto() + " | Código: " + p.getCodebar());
            System.out.println("   Precio: $" + p.getPrecio() + " | Stock: " + p.getCantidad());
            System.out.println("   Laboratorio: " + p.getLaboratorio() + " | Rubro: " + p.getRubro());
        }

        if (productos.size() > 10) {
            System.out.println("\nℹ️ Use la opción de importación desde Excel para cargar más productos");
        }
    }

    private static void mostrarProductosStockBajo() {
        System.out.println("\n=== PRODUCTOS CON STOCK BAJO (< 10 UNIDADES) ===");

        ProductoService productoService = new ProductoService();
        String sql = "SELECT * FROM productos WHERE cantidad > 0 AND cantidad < 10 ORDER BY cantidad ASC";

        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            int contador = 0;
            System.out.println("\n📦 Productos con stock bajo:");
            System.out.println("-".repeat(80));

            while (rs.next()) {
                contador++;
                System.out.printf("%2d. %-40s | Stock: %2d | Precio: $%8.2f%n",
                        contador,
                        rs.getString("producto"),
                        rs.getInt("cantidad"),
                        rs.getBigDecimal("precio"));
            }

            if (contador == 0) {
                System.out.println("✅ No hay productos con stock bajo");
            } else {
                System.out.println("-".repeat(80));
                System.out.println("📊 Total: " + contador + " productos con stock bajo");
            }

        } catch (Exception e) {
            System.err.println("❌ Error al obtener productos con stock bajo: " + e.getMessage());
        }
    }

    private static void mostrarProductosSinStock() {
        System.out.println("\n=== PRODUCTOS SIN STOCK ===");

        ProductoService productoService = new ProductoService();
        String sql = "SELECT * FROM productos WHERE cantidad = 0 ORDER BY producto ASC";

        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            int contador = 0;
            System.out.println("\n📦 Productos sin stock:");
            System.out.println("-".repeat(80));

            while (rs.next()) {
                contador++;
                System.out.printf("%2d. %-50s | Precio: $%8.2f%n",
                        contador,
                        rs.getString("producto"),
                        rs.getBigDecimal("precio"));

                // Mostrar máximo 20 productos
                if (contador >= 20) {
                    System.out.println("... (mostrando primeros 20 productos)");
                    break;
                }
            }

            if (contador == 0) {
                System.out.println("✅ No hay productos sin stock");
            } else {
                System.out.println("-".repeat(80));
                System.out.println("📊 Total: " + contador + " productos sin stock");
            }

        } catch (Exception e) {
            System.err.println("❌ Error al obtener productos sin stock: " + e.getMessage());
        }
    }
}