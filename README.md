# Sistema de Gestión Farmacéutica

## 📋 Descripción
Sistema de gestión integral para farmacias desarrollado en Java con interfaz desktop. Permite control de stock, ventas por código de barras y gestión de productos.

## 🚀 Estado Actual
**Fase 1 Completada** ✅
- Estructura del proyecto Maven configurada
- Base de datos SQLite operativa
- Modelo de datos implementado
- Conexión a BD funcionando

## 🛠 Tecnologías
- **Java 21**
- **Maven** - Gestión de dependencias
- **SQLite** - Base de datos embebida
- **JavaFX** - Interfaz gráfica (próxima fase)
- **Apache POI** - Importación desde Excel

## 📊 Estructura de la Base de Datos
```sql
Tabla: productos
Campos principales: idproducto, codebar, producto, precio, laboratorio, rubro, droga, cantidad
Índices optimizados para búsquedas rápidas
