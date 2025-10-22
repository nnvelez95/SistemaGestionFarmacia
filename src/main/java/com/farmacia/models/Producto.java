package com.farmacia.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Producto {
    private String idproducto;
    private String troquel;
    private String codebar;
    private String codebars;
    private String producto;
    private String pcto;
    private String presentacion;
    private int cantidad;
    private BigDecimal costo;
    private BigDecimal precio;
    private LocalDateTime fechaUltimoPrecio;
    private BigDecimal alicuotaiva;
    private BigDecimal preciopami;
    private String laboratorio;
    private String rubro;
    private String rubroLetra;
    private String droga;

    // Constructores
    public Producto() {}

    // Getters y setters
    public String getIdproducto() { return idproducto; }
    public void setIdproducto(String idproducto) { this.idproducto = idproducto; }

    public String getTroquel() { return troquel; }
    public void setTroquel(String troquel) { this.troquel = troquel; }

    public String getCodebar() { return codebar; }
    public void setCodebar(String codebar) { this.codebar = codebar; }

    public String getCodebars() { return codebars; }
    public void setCodebars(String codebars) { this.codebars = codebars; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public String getPcto() { return pcto; }
    public void setPcto(String pcto) { this.pcto = pcto; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public LocalDateTime getFechaUltimoPrecio() { return fechaUltimoPrecio; }
    public void setFechaUltimoPrecio(LocalDateTime fechaUltimoPrecio) { this.fechaUltimoPrecio = fechaUltimoPrecio; }

    public BigDecimal getAlicuotaiva() { return alicuotaiva; }
    public void setAlicuotaiva(BigDecimal alicuotaiva) { this.alicuotaiva = alicuotaiva; }

    public BigDecimal getPreciopami() { return preciopami; }
    public void setPreciopami(BigDecimal preciopami) { this.preciopami = preciopami; }

    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }

    public String getRubro() { return rubro; }
    public void setRubro(String rubro) { this.rubro = rubro; }

    public String getRubroLetra() { return rubroLetra; }
    public void setRubroLetra(String rubroLetra) { this.rubroLetra = rubroLetra; }

    public String getDroga() { return droga; }
    public void setDroga(String droga) { this.droga = droga; }

    @Override
    public String toString() {
        return producto + " - " + precio + " - Stock: " + cantidad;
    }
}