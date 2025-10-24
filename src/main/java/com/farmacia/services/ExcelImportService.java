package com.farmacia.services;

import com.farmacia.models.Producto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  // Para .xls
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Para .xlsx

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExcelImportService {

    private static final int BATCH_SIZE = 1000;

    public ImportResult importarProductosDesdeExcel(String filePath) {
        ImportResult result = new ImportResult();

        try {
            Workbook workbook = crearWorkbook(filePath);

            try {
                Sheet sheet = workbook.getSheetAt(0);
                result.totalRegistros = sheet.getLastRowNum();

                System.out.println("📊 Iniciando importación de " + result.totalRegistros + " registros...");
                System.out.println("📁 Formato del archivo: " + (filePath.toLowerCase().endsWith(".xls") ? "XLS (OLE2)" : "XLSX (OOXML)"));

                // Procesar por lotes
                for (int startRow = 1; startRow <= result.totalRegistros; startRow += BATCH_SIZE) {
                    int endRow = Math.min(startRow + BATCH_SIZE - 1, result.totalRegistros);
                    procesarLote(sheet, startRow, endRow, result);

                    if (startRow % 5000 == 1 || endRow >= result.totalRegistros) {
                        System.out.printf("🔄 Procesado lote: %,d - %,d (%,d/%,d)%n",
                                startRow, endRow, endRow, result.totalRegistros);
                    }
                }

            } finally {
                workbook.close();
            }

        } catch (Exception e) {
            System.err.println("❌ Error crítico durante importación: " + e.getMessage());
            result.errores++;
        }

        return result;
    }

    private Workbook crearWorkbook(String filePath) throws Exception {
        FileInputStream file = new FileInputStream(new File(filePath));

        if (filePath.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(file);  // Para .xlsx
        } else if (filePath.toLowerCase().endsWith(".xls")) {
            return new HSSFWorkbook(file);  // Para .xls
        } else {
            throw new IllegalArgumentException("Formato de archivo no soportado: " + filePath);
        }
    }

    private void procesarLote(Sheet sheet, int startRow, int endRow, ImportResult result) {
        List<Producto> loteProductos = new ArrayList<>();

        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                try {
                    Producto producto = mapearFilaAProducto(row);
                    if (producto != null && validarProducto(producto)) {
                        loteProductos.add(producto);
                    } else {
                        result.errores++;
                    }
                } catch (Exception e) {
                    result.errores++;
                    // No imprimir cada error individual para no saturar la consola
                }
            }
        }

        // Insertar lote en base de datos
        insertarLoteEnBD(loteProductos, result);
    }

    private void insertarLoteEnBD(List<Producto> productos, ImportResult result) {
        if (productos.isEmpty()) return;

        String sql = "INSERT OR REPLACE INTO productos (idproducto, troquel, codebar, codebars, producto, pcto, presentacion, cantidad, costo, precio, alicuotaiva, preciopami, laboratorio, rubro, rubro_letra, droga) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = com.farmacia.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Producto producto : productos) {
                try {
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
                    pstmt.setBigDecimal(11, producto.getAlicuotaiva());
                    pstmt.setBigDecimal(12, producto.getPreciopami());
                    pstmt.setString(13, producto.getLaboratorio());
                    pstmt.setString(14, producto.getRubro());
                    pstmt.setString(15, producto.getRubroLetra());
                    pstmt.setString(16, producto.getDroga());

                    pstmt.addBatch();
                } catch (Exception e) {
                    result.errores++;
                }
            }

            // Ejecutar todo el lote
            int[] resultados = pstmt.executeBatch();
            for (int res : resultados) {
                if (res > 0) {
                    result.importadosExitosos++;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error en lote BD: " + e.getMessage());
            result.errores += productos.size();
        }
    }

    private Producto mapearFilaAProducto(Row row) {
        try {
            Producto producto = new Producto();

            // Mapear campos según la estructura del Excel
            producto.setIdproducto(getCellStringValue(row.getCell(0))); // idproducto
            producto.setTroquel(getCellStringValue(row.getCell(1)));    // troquel
            producto.setCodebar(getCellStringValue(row.getCell(2)));    // codebar
            producto.setCodebars(getCellStringValue(row.getCell(3)));   // codebars
            producto.setProducto(getCellStringValue(row.getCell(4)));   // producto
            producto.setPcto(getCellStringValue(row.getCell(5)));       // pcto
            producto.setPresentacion(getCellStringValue(row.getCell(6))); // presentacion

            // Campos numéricos
            producto.setCantidad(getCellIntValue(row.getCell(7), 0));   // cantidad
            producto.setCosto(getCellBigDecimalValue(row.getCell(8)));  // costo
            producto.setPrecio(getCellBigDecimalValue(row.getCell(9))); // precio

            // Campos adicionales
            producto.setAlicuotaiva(getCellBigDecimalValue(row.getCell(11))); // alicuotaiva
            producto.setPreciopami(getCellBigDecimalValue(row.getCell(12)));  // preciopami
            producto.setLaboratorio(getCellStringValue(row.getCell(13)));     // laboratorio
            producto.setRubro(getCellStringValue(row.getCell(14)));           // rubro
            producto.setRubroLetra(getCellStringValue(row.getCell(15)));      // rubro_letra
            producto.setDroga(getCellStringValue(row.getCell(16)));           // droga

            return producto;

        } catch (Exception e) {
            return null;
        }
    }

    private boolean validarProducto(Producto producto) {
        // Validaciones básicas
        if (producto.getIdproducto() == null || producto.getIdproducto().trim().isEmpty()) {
            return false;
        }
        if (producto.getProducto() == null || producto.getProducto().trim().isEmpty()) {
            return false;
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        return true;
    }

    // Métodos auxiliares para leer celdas
    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double num = cell.getNumericCellValue();
                    // Si es número entero, quitar decimales
                    if (num == (long) num) {
                        return String.valueOf((long) num);
                    } else {
                        return String.valueOf(num);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }

    private int getCellIntValue(Cell cell, int defaultValue) {
        try {
            if (cell == null) return defaultValue;

            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) cell.getNumericCellValue();
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? defaultValue : Integer.parseInt(value);
                default:
                    return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private BigDecimal getCellBigDecimalValue(Cell cell) {
        try {
            if (cell == null) return BigDecimal.ZERO;

            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String value = cell.getStringCellValue().trim().replace(",", ".");
                    return value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);
                case FORMULA:
                    try {
                        return BigDecimal.valueOf(cell.getNumericCellValue());
                    } catch (Exception e) {
                        return BigDecimal.ZERO;
                    }
                default:
                    return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    // Clase para resultados de importación
    public static class ImportResult {
        public int totalRegistros;
        public int importadosExitosos;
        public int errores;

        public double getPorcentajeExito() {
            return totalRegistros > 0 ? (importadosExitosos * 100.0) / totalRegistros : 0;
        }
    }
}