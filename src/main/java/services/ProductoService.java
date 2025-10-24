package com.farmacia.services;

import com.farmacia.database.DatabaseConnection;
import com.farmacia.models.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    // INSERTAR producto
    public boolean insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (idproducto, troquel, codebar, codebars, producto, pcto, presentacion, cantidad, costo, precio, fecha_ultimo_precio, alicuotaiva, preciopami, laboratorio, rubro, rubro_letra, droga) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, producto.getIdproducto());
            pstmt.setString(2, producto.getTroquel());
            pstmt.setString(3, producto.getCodebar());
            pstmt.setString(4, producto.getCodebars());
            pstmt.setString(5, producto.getProducto());
            pstmt.setString(6, producto.getPcto());
            pstmt.setString(7, producto.getPresentacion());
            pstmt.setInt(8, producto.getCantidad());
            pstmt.setBigDecimal(9, producto.getCosto());
            pstmt.setBigDecimal(10, producto.getPrecio());
            pstmt.setString(11, producto.getFechaUltimoPrecio() != null ? producto.getFechaUltimoPrecio().toString() : null);
            pstmt.setBigDecimal(12, producto.getAlicuotaiva());
            pstmt.setBigDecimal(13, producto.getPreciopami());
            pstmt.setString(14, producto.getLaboratorio());
            pstmt.setString(15, producto.getRubro());
            pstmt.setString(16, producto.getRubroLetra());
            pstmt.setString(17, producto.getDroga());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    // OBTENER producto por ID
    public Producto obtenerPorId(String idProducto) {
        String sql = "SELECT * FROM productos WHERE idproducto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idProducto);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapearResultSetAProducto(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener producto: " + e.getMessage());
        }

        return null;
    }

    // OBTENER producto por código de barras
    public Producto obtenerPorCodigoBarras(String codigoBarras) {
        String sql = "SELECT * FROM productos WHERE codebar = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoBarras);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapearResultSetAProducto(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener producto por código de barras: " + e.getMessage());
        }

        return null;
    }

    // OBTENER todos los productos
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapearResultSetAProducto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
        }

        return productos;
    }

    // ACTUALIZAR producto
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET troquel = ?, codebar = ?, codebars = ?, producto = ?, pcto = ?, presentacion = ?, cantidad = ?, costo = ?, precio = ?, fecha_ultimo_precio = ?, alicuotaiva = ?, preciopami = ?, laboratorio = ?, rubro = ?, rubro_letra = ?, droga = ? WHERE idproducto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, producto.getTroquel());
            pstmt.setString(2, producto.getCodebar());
            pstmt.setString(3, producto.getCodebars());
            pstmt.setString(4, producto.getProducto());
            pstmt.setString(5, producto.getPcto());
            pstmt.setString(6, producto.getPresentacion());
            pstmt.setInt(7, producto.getCantidad());
            pstmt.setBigDecimal(8, producto.getCosto());
            pstmt.setBigDecimal(9, producto.getPrecio());
            pstmt.setString(10, producto.getFechaUltimoPrecio() != null ? producto.getFechaUltimoPrecio().toString() : null);
            pstmt.setBigDecimal(11, producto.getAlicuotaiva());
            pstmt.setBigDecimal(12, producto.getPreciopami());
            pstmt.setString(13, producto.getLaboratorio());
            pstmt.setString(14, producto.getRubro());
            pstmt.setString(15, producto.getRubroLetra());
            pstmt.setString(16, producto.getDroga());
            pstmt.setString(17, producto.getIdproducto());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR producto
    public boolean eliminarProducto(String idProducto) {
        String sql = "DELETE FROM productos WHERE idproducto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idProducto);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para mapear ResultSet a Producto
    private Producto mapearResultSetAProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdproducto(rs.getString("idproducto"));
        producto.setTroquel(rs.getString("troquel"));
        producto.setCodebar(rs.getString("codebar"));
        producto.setCodebars(rs.getString("codebars"));
        producto.setProducto(rs.getString("producto"));
        producto.setPcto(rs.getString("pcto"));
        producto.setPresentacion(rs.getString("presentacion"));
        producto.setCantidad(rs.getInt("cantidad"));
        producto.setCosto(rs.getBigDecimal("costo"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        // Para fecha podríamos hacer parsing más adelante
        producto.setAlicuotaiva(rs.getBigDecimal("alicuotaiva"));
        producto.setPreciopami(rs.getBigDecimal("preciopami"));
        producto.setLaboratorio(rs.getString("laboratorio"));
        producto.setRubro(rs.getString("rubro"));
        producto.setRubroLetra(rs.getString("rubro_letra"));
        producto.setDroga(rs.getString("droga"));

        return producto;
    }
}