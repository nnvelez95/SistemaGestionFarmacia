package com.farmacia.services;

import com.farmacia.database.DatabaseConnection;
import com.farmacia.models.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RecepcionPedidosService {

    private Scanner scanner;
    private ProductoService productoService;

    public RecepcionPedidosService(Scanner scanner) {
        this.scanner = scanner;
        this.productoService = new ProductoService();
    }

    // MÉTODO PRINCIPAL - RECEPCIÓN DE PEDIDO
    public void recepcionarPedido() {
        System.out.println("\n=== RECEPCIÓN DE PEDIDO ===");
        System.out.println("📦 Sistema de ingreso de mercadería");

        List<ItemPedido> itemsPedido = new ArrayList<>();
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("¿Cómo desea buscar el producto?");
            System.out.println("1. Escanear código de barras");
            System.out.println("2. Buscar por nombre (búsqueda parcial)");
            System.out.println("3. Finalizar recepción");
            System.out.print("Seleccione opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    agregarProductoPorCodigoBarras(itemsPedido);
                    break;
                case "2":
                    agregarProductoPorNombre(itemsPedido);
                    break;
                case "3":
                    continuar = false;
                    break;
                default:
                    System.out.println("❌ Opción no válida");
            }
        }

        // Si hay items en el pedido, procesar
        if (!itemsPedido.isEmpty()) {
            procesarRecepcion(itemsPedido);
        } else {
            System.out.println("📭 Pedido vacío - No se procesó ninguna recepción");
        }
    }

    // MÉTODO 1: AGREGAR POR CÓDIGO DE BARRAS
    private void agregarProductoPorCodigoBarras(List<ItemPedido> itemsPedido) {
        System.out.print("\n📊 Escanee o ingrese código de barras: ");
        String codigoBarras = scanner.nextLine().trim();

        if (codigoBarras.isEmpty()) {
            System.out.println("❌ Código de barras vacío");
            return;
        }

        Producto producto = productoService.obtenerPorCodigoBarras(codigoBarras);

        if (producto == null) {
            System.out.println("❌ Producto no encontrado con código: " + codigoBarras);
            System.out.print("¿Desea buscar por nombre? (s/n): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                agregarProductoPorNombre(itemsPedido);
            }
            return;
        }

        // Producto encontrado, solicitar cantidad
        procesarCantidadProducto(producto, itemsPedido);
    }

    // MÉTODO 2: AGREGAR POR NOMBRE (BÚSQUEDA PARCIAL)
    private void agregarProductoPorNombre(List<ItemPedido> itemsPedido) {
        System.out.print("\n🔍 Ingrese parte del nombre del producto: ");
        String busqueda = scanner.nextLine().trim();

        if (busqueda.isEmpty()) {
            System.out.println("❌ Término de búsqueda vacío");
            return;
        }

        if (busqueda.length() < 3) {
            System.out.println("⚠️ Ingrese al menos 3 caracteres para la búsqueda");
            return;
        }

        List<Producto> resultados = buscarProductosPorNombreParcial(busqueda);

        if (resultados.isEmpty()) {
            System.out.println("❌ No se encontraron productos con: '" + busqueda + "'");
            return;
        }

        // Mostrar resultados
        System.out.println("\n📋 PRODUCTOS ENCONTRADOS:");
        System.out.println("-".repeat(80));

        for (int i = 0; i < resultados.size(); i++) {
            Producto p = resultados.get(i);
            System.out.printf("%2d. %-50s | Stock: %3d | Precio: $%8.2f%n",
                    i + 1, p.getProducto(), p.getCantidad(), p.getPrecio());
        }

        System.out.print("\nSeleccione el producto (1-" + resultados.size() + ") o 0 para cancelar: ");

        try {
            int seleccion = Integer.parseInt(scanner.nextLine());

            if (seleccion == 0) {
                System.out.println("✅ Búsqueda cancelada");
                return;
            }

            if (seleccion < 1 || seleccion > resultados.size()) {
                System.out.println("❌ Selección inválida");
                return;
            }

            Producto productoSeleccionado = resultados.get(seleccion - 1);
            procesarCantidadProducto(productoSeleccionado, itemsPedido);

        } catch (NumberFormatException e) {
            System.out.println("❌ Ingrese un número válido");
        }
    }

    // MÉTODO 3: PROCESAR CANTIDAD DEL PRODUCTO
    private void procesarCantidadProducto(Producto producto, List<ItemPedido> itemsPedido) {
        System.out.println("\n✅ PRODUCTO SELECCIONADO:");
        System.out.println("   📝 " + producto.getProducto());
        System.out.println("   📊 Código: " + producto.getCodebar());
        System.out.println("   📦 Stock actual: " + producto.getCantidad() + " unidades");

        System.out.print("\nIngrese cantidad recibida: ");

        try {
            int cantidad = Integer.parseInt(scanner.nextLine().trim());

            if (cantidad <= 0) {
                System.out.println("❌ La cantidad debe ser mayor a 0");
                return;
            }

            // Verificar si el producto ya está en el pedido
            ItemPedido itemExistente = buscarItemEnPedido(itemsPedido, producto.getIdproducto());

            if (itemExistente != null) {
                // Actualizar cantidad existente
                itemExistente.cantidad += cantidad;
                System.out.println("📦 Cantidad actualizada: " + itemExistente.cantidad + " unidades");
            } else {
                // Agregar nuevo item
                ItemPedido nuevoItem = new ItemPedido(producto, cantidad);
                itemsPedido.add(nuevoItem);
                System.out.println("✅ Producto agregado al pedido: " + cantidad + " unidades");
            }

            // Mostrar resumen actual del pedido
            mostrarResumenPedido(itemsPedido);

        } catch (NumberFormatException e) {
            System.out.println("❌ Cantidad inválida");
        }
    }

    // MÉTODO 4: PROCESAR RECEPCIÓN COMPLETA
    private void procesarRecepcion(List<ItemPedido> itemsPedido) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📦 RESUMEN FINAL DEL PEDIDO");
        System.out.println("=".repeat(60));

        int totalUnidades = 0;

        for (ItemPedido item : itemsPedido) {
            System.out.printf("📝 %-40s | +%3d unidades | Stock nuevo: %3d%n",
                    item.producto.getProducto(),
                    item.cantidad,
                    item.producto.getCantidad() + item.cantidad);
            totalUnidades += item.cantidad;
        }

        System.out.println("-".repeat(60));
        System.out.println("📊 Total de productos: " + itemsPedido.size());
        System.out.println("📦 Total de unidades: " + totalUnidades);
        System.out.println("=".repeat(60));

        System.out.print("\n¿Confirmar recepción del pedido? (s/n): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("s")) {
            // Actualizar stock en base de datos
            boolean exito = actualizarStockEnBD(itemsPedido);

            if (exito) {
                System.out.println("\n🎉 ¡RECEPCIÓN EXITOSA!");
                System.out.println("✅ Stock actualizado correctamente");
                generarTicketRecepcion(itemsPedido);
            } else {
                System.out.println("❌ Error al actualizar el stock");
            }
        } else {
            System.out.println("❌ Recepción cancelada");
        }
    }

    // MÉTODO 5: ACTUALIZAR STOCK EN BASE DE DATOS
    private boolean actualizarStockEnBD(List<ItemPedido> itemsPedido) {
        String sql = "UPDATE productos SET cantidad = cantidad + ? WHERE idproducto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (ItemPedido item : itemsPedido) {
                pstmt.setInt(1, item.cantidad);
                pstmt.setString(2, item.producto.getIdproducto());
                pstmt.addBatch();
            }

            int[] resultados = pstmt.executeBatch();

            // Verificar que todas las actualizaciones fueron exitosas
            for (int resultado : resultados) {
                if (resultado <= 0) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.err.println("❌ Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    // MÉTODO 6: GENERAR TICKET DE RECEPCIÓN
    private void generarTicketRecepcion(List<ItemPedido> itemsPedido) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                  🏥 TICKET DE RECEPCIÓN");
        System.out.println("=".repeat(70));
        System.out.println("Fecha: " + java.time.LocalDateTime.now());
        System.out.println("-".repeat(70));

        int totalUnidades = 0;
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedido item : itemsPedido) {
            BigDecimal valorItem = item.producto.getPrecio().multiply(BigDecimal.valueOf(item.cantidad));
            System.out.printf("%-40s %3d x $%8.2f = $%10.2f%n",
                    item.producto.getProducto(),
                    item.cantidad,
                    item.producto.getPrecio(),
                    valorItem);

            totalUnidades += item.cantidad;
            valorTotal = valorTotal.add(valorItem);
        }

        System.out.println("-".repeat(70));
        System.out.printf("TOTAL: %d productos, %d unidades%n", itemsPedido.size(), totalUnidades);
        System.out.printf("VALOR TOTAL DEL PEDIDO: $%.2f%n", valorTotal);
        System.out.println("=".repeat(70));
    }

    // MÉTODOS AUXILIARES
    private List<Producto> buscarProductosPorNombreParcial(String busqueda) {
        List<Producto> resultados = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE producto LIKE ? ORDER BY producto LIMIT 15";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + busqueda + "%");
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

        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }

        return resultados;
    }

    private ItemPedido buscarItemEnPedido(List<ItemPedido> itemsPedido, String idProducto) {
        return itemsPedido.stream()
                .filter(item -> item.producto.getIdproducto().equals(idProducto))
                .findFirst()
                .orElse(null);
    }

    private void mostrarResumenPedido(List<ItemPedido> itemsPedido) {
        if (itemsPedido.isEmpty()) return;

        System.out.println("\n📋 RESUMEN PARCIAL DEL PEDIDO:");
        System.out.println("-".repeat(50));

        for (ItemPedido item : itemsPedido) {
            System.out.printf("   %-30s | +%d unidades%n",
                    item.producto.getProducto(), item.cantidad);
        }

        int totalItems = itemsPedido.size();
        int totalUnidades = itemsPedido.stream().mapToInt(item -> item.cantidad).sum();

        System.out.println("-".repeat(50));
        System.out.printf("   Total: %d productos, %d unidades%n", totalItems, totalUnidades);
    }

    // CLASE INTERNA PARA ITEMS DEL PEDIDO
    private static class ItemPedido {
        Producto producto;
        int cantidad;

        ItemPedido(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }
    }
}