# **Sistema de Gestión Farmacéutica 🏥**

## **📋 Descripción**

Sistema de gestión integral para farmacias desarrollado en **Java** con interfaz desktop. Permite control de stock, ventas por código de barras y gestión de productos.

## **🚀 Evolución del Proyecto**

### **Fase 1: Estructura Base y Configuración**

| Atributo | Detalle |
| :---- | :---- |
| **Fecha** | Octubre 2025 |
| **Estado** | ✅ **COMPLETADA** |

#### **🎯 Objetivos Cumplidos:**

* ✅ Proyecto Maven configurado con Java 21
* ✅ Base de datos **SQLite** implementada
* ✅ Tabla 'productos' creada con índices optimizados
* ✅ Modelo de datos Producto definido
* ✅ Conexión a BD funcionando correctamente
* ✅ Dependencias: SQLite JDBC, JavaFX, Apache POI

#### **📁 Estructura Creada:**

src/main/java/com/farmacia/  
├── database/  
│ ├── DatabaseConnection.java  
│ └── DatabaseInitializer.java  
├── models/  
│ └── Producto.java  
└── Main.java

### **Fase 2: Servicios CRUD y Operaciones BD**

| Atributo | Detalle |
| :---- | :---- |
| **Fecha** | Octubre 2025 |
| **Estado** | ✅ **COMPLETADA** |

#### **🎯 Objetivos Cumplidos:**

* ✅ Clase ProductoService con operaciones completas CRUD
* ✅ Implementar INSERT, SELECT, UPDATE, DELETE
* ✅ Búsquedas por ID y código de barras
* ✅ Pruebas de funcionalidad con datos de prueba
* ✅ Verificación de operaciones en base de datos

#### **🔧 Funcionalidades Implementadas (ProductoService):**

\* insertarProducto(Producto) // CREATE  
\* obtenerPorId(String) // READ  
\* obtenerPorCodigoBarras(String) // READ  
\* obtenerTodos() // READ ALL  
\* actualizarProducto(Producto) // UPDATE  
\* eliminarProducto(String) // DELETE

#### **📁 Estructura Ampliada:**

src/main/java/com/farmacia/  
├── services/  
│   └── ProductoService.java

#### **🧪 Pruebas Ejecutadas:**

* ✓ Inserción de producto de prueba
* ✓ Consulta por ID y código de barras
* ✓ Actualización de precio y stock
* ✓ Listado completo de productos
* ✓ Manejo de errores y conexiones

### **Fase 3: Importación, Recepción y Módulos Avanzados**

| Atributo | Detalle |
| :---- | :---- |
| **Fecha** | Noviembre 2025 |
| **Estado** | ✅ **COMPLETADA** |
| **Versión** | **V1.0 SISTEMA OPERATIVO COMPLETO** |

#### **🎯 Objetivos Cumplidos (V1.0):**

* ✅ Implementación completa de la **Gestión Individual de Productos** (CRUD, Búsquedas Avanzadas)
* ✅ Creación del **Módulo de Recepción** con actualización automática de stock y ticketing.
* ✅ Implementación de **Operaciones Masivas** (Importación y Actualización de Precios desde Excel).
* ✅ Desarrollo del **Módulo de Informes** (Stock bajo/sin stock, estadísticas generales).
* ✅ Refactorización de la arquitectura para el manejo eficiente de grandes volúmenes de datos (+50K registros).

#### **🔧 Módulos de Servicio Implementados:**

| Módulo de Servicio | Funcionalidad Principal |
| :---- | :---- |
| GestionProductosService | Lógica de negocio para CRUD individual y búsquedas avanzadas. |
| RecepcionPedidosService | Manejo completo del flujo de recepción y actualización de inventario. |
| ExcelImportService | Lógica de importación masiva de productos (50,000+ registros). |
| ExcelUpdateService | Lógica de actualización masiva de precios y stock. |

### **Fase 4: Interfaz JavaFX**

| Atributo | Detalle |
| :---- | :---- |
| **Fecha** | Futuro |
| **Estado** | ⏳ **PENDIENTE** |

#### **🎯 Objetivos:**

* 🖥️ Configuración de JavaFX
* 🎨 Diseño de pantallas de gestión
* 🔗 Conexión de interfaz con servicios

### **Fase 5: Módulo de Ventas**

| Atributo | Detalle |
| :---- | :---- |
| **Fecha** | Futuro |
| **Estado** | ⏳ **PENDIENTE** |

#### **🎯 Objetivos:**

* 🧪 Sistema de tickets y caja
* 💰 Venta rápida por código de barras
* 📦 Control de stock en tiempo real

## **🛠 Stack Tecnológico**

| Tecnología | Rol |
| :---- | :---- |
| **Java 21** | Lenguaje principal |
| **Maven** | Gestión de dependencias |
| **SQLite** | Base de datos embebida |
| **JDBC** | Conexión a base de datos |
| **JavaFX** | Interfaz gráfica (Fase 4\) |
| **Apache POI** | Importación Excel (Fase 3\) |

## **📊 Estructura de Base de Datos**

La base de datos utiliza una tabla principal para almacenar la información de los productos.

CREATE TABLE productos (  
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
fecha\_ultimo\_precio DATETIME,  
alicuotaiva DECIMAL(5,2) DEFAULT 0,  
preciopami DECIMAL(10,2) DEFAULT 0,  
laboratorio VARCHAR(100),  
rubro VARCHAR(50),  
rubro\_letra CHAR(1),  
droga VARCHAR(255)  
);

## **⚡ Ejecución del Proyecto**

Para compilar y ejecutar el proyecto desde la línea de comandos (asumiendo que tienes Maven y Java 21 configurados):

\# Compilar y ejecutar  
mvn clean compile exec:java

\# Solo compilar  
mvn clean compile

\# Ejecutar tests  
mvn test

## **📈 Métricas Actuales**

* ✅ **3 Fases completadas**
* 🏆 **Sistema Operativo Completo (V1.0)**
* 🔧 5 Módulos de Servicio implementados (ProductoService, GestionProductosService, RecepcionPedidosService, ExcelImportService, ExcelUpdateService)
* 📊 Capacidad para manejar más de 50,000 registros.

## **🗂️ Estructura Completa del Proyecto**

SistemaGestionFarmacia/  
├── src/  
│ └── main/  
│ ├── java/  
│ │ └── com/  
│ │ └── farmacia/  
│ │ ├── Main.java  
│ │ ├── database/  
│ │ │ ├── DatabaseConnection.java  
│ │ │ └── DatabaseInitializer.java  
│ │ ├── models/  
│ │ │ └── Producto.java  
│ │ └── services/  
│ │ ├── ProductoService.java  
│ │ ├── ExcelImportService.java  
│ │ ├── ExcelUpdateService.java  
│ │ ├── GestionProductosService.java  
│ │ └── RecepcionPedidosService.java  
│ └── resources/  
├── pom.xml  
└── farmacia.db (generado automáticamente)

## **🔄 Flujo de Desarrollo**

1. **Configuración** → Estructura del proyecto
2. **Persistencia** → Base de datos y modelos
3. **Servicios** → Lógica de negocio (CRUD)
4. **Módulos Avanzados** → Importación, Recepción y Gestión Detallada (V1.0 COMPLETA)
5. **Interfaz** → JavaFX para usuarios
6. **Ventas** → Módulo comercial completo

*Proyecto educativo desarrollado para aplicar conceptos de Java, bases de datos y desarrollo de software profesional.*

## **👨‍💻 Autor**

* **Nicolás Vélez**
* **📧** [nnvelez95@gmail.com](mailto:nnvelez95@gmail.com)

## **📄 Licencia**

Este proyecto es de código abierto y está disponible bajo la **Licencia MIT**.