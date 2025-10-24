package com.farmacia.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ExcelUpdateService {

    public UpdateResult actualizarSoloPrecios(String filePath) {
        UpdateResult result = new UpdateResult();

        try {
            Workbook workbook = crearWorkbook(filePath);

            try {
                Sheet sheet = workbook.getSheetAt(0);
                result.totalRegistros = sheet.getLastRowNum();

                System.out.println("💰 Actualizando precios desde " + result.totalRegistros + " registros...");

                String sql = "UPDATE productos SET precio = ? WHERE idproducto = ?";

                try (Connection conn = com.farmacia.database.DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    int contador = 0;

                    for (int i = 1; i <= result.totalRegistros; i++) {
                        Row row = sheet.getRow(i);
                        if (row != null) {
                            try {
                                String idProducto = getCellStringValue(row.getCell(0)); // Columna A: idproducto
                                BigDecimal nuevoPrecio = getCellBigDecimalValue(row.getCell(9)); // Columna J: precio

                                if (idProducto != null && !idProducto.trim().isEmpty() &&
                                        nuevoPrecio != null && nuevoPrecio.compareTo(BigDecimal.ZERO) >= 0) {

                                    pstmt.setBigDecimal(1, nuevoPrecio);
                                    pstmt.setString(2, idProducto);
                                    pstmt.addBatch();

                                    contador++;
                                    result.registrosProcesados++;

                                    // Ejecutar cada 1000 registros para no saturar memoria
                                    if (contador % 1000 == 0) {
                                        int[] resultados = pstmt.executeBatch();
                                        for (int res : resultados) {
                                            if (res > 0) result.actualizadosExitosos++;
                                            else result.productosNoEncontrados++;
                                        }
                                        System.out.printf("🔄 Procesados: %,d/%,d%n", i, result.totalRegistros);
                                        contador = 0;
                                    }
                                }

                            } catch (Exception e) {
                                result.errores++;
                            }
                        }
                    }

                    // Ejecutar el último lote
                    if (contador > 0) {
                        int[] resultados = pstmt.executeBatch();
                        for (int res : resultados) {
                            if (res > 0) result.actualizadosExitosos++;
                            else result.productosNoEncontrados++;
                        }
                    }

                }

            } finally {
                workbook.close();
            }

        } catch (Exception e) {
            System.err.println("❌ Error durante actualización: " + e.getMessage());
            result.errores++;
        }

        return result;
    }

    // Métodos auxiliares
    private Workbook crearWorkbook(String filePath) throws Exception {
        FileInputStream file = new FileInputStream(new File(filePath));

        if (filePath.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(file);
        } else if (filePath.toLowerCase().endsWith(".xls")) {
            return new HSSFWorkbook(file);
        } else {
            throw new IllegalArgumentException("Formato no soportado: " + filePath);
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC:
                double num = cell.getNumericCellValue();
                return num == (long) num ? String.valueOf((long) num) : String.valueOf(num);
            default: return "";
        }
    }

    private BigDecimal getCellBigDecimalValue(Cell cell) {
        try {
            if (cell == null) return BigDecimal.ZERO;
            switch (cell.getCellType()) {
                case NUMERIC: return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String value = cell.getStringCellValue().trim().replace(",", ".");
                    return value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);
                default: return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public static class UpdateResult {
        public int totalRegistros;
        public int registrosProcesados;
        public int actualizadosExitosos;
        public int productosNoEncontrados;
        public int errores;

        public double getPorcentajeExito() {
            return registrosProcesados > 0 ? (actualizadosExitosos * 100.0) / registrosProcesados : 0;
        }
    }
}