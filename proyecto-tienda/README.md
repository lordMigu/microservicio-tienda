
# Proyecto Tienda – Microservicio de Ventas (Spring Boot)

Microservicio REST para gestionar **Clientes**, **Productos** y **Ventas**, con operaciones CRUD y consultas específicas (venta de mayor monto, resumen diario, productos con bajo stock).

> Tech stack: Java + Spring Boot (Web, Data JPA), Lombok, JSON (Jackson). Base de datos configurable vía `application.properties`.

---

## ⚙️ Arquitectura y Modelo de Dominio

- **Entidad `Cliente`**  
  Campos: `id_cliente`, `nombre`, `apellido`, `dni`.  
  Relación: asociada **1:1** con `Venta` (lado cliente referenciado desde `Venta` con Jackson `@JsonManagedReference`/`@JsonBackReference` para evitar ciclos).

- **Entidad `Producto`**  
  Campos: `codigo_producto`, `nombre`, `marca`, `costo`, `cantidad_disponible`.  
  Relación: **N:1** con `Venta` (cada producto pertenece a una venta; desde `Venta` se expone `listaProductos`).

- **Entidad `Venta`**  
  Campos: `codigo_venta`, `fecha_venta` (`LocalDate`), `total`, `listaProductos`, `unCliente` (1:1).

- **DTO `VentaMayorDTO`**  
  Campos: `codigo_venta`, `total`, `cantidad_productos`, `nombre_cliente`, `apellido_cliente`.

> Las anotaciones JPA/Hibernate y Jackson (`@JsonManagedReference`, `@JsonBackReference`) están presentes en las entidades para mapear relaciones y serialización JSON.

---

## � Relaciones JPA: @OneToOne vs @OneToMany

Este proyecto implementa dos tipos de relaciones JPA fundamentales:

### @OneToOne (Uno a Uno) – Cliente ↔ Venta
Una entidad `Cliente` se asocia con **exactamente una** entidad `Venta`. Esta relación es de exclusividad.
- **Ubicación del FK**: Puede estar en cualquiera de las dos tablas; el lado con `@JoinColumn` es el dueño.
- **Fetch Strategy**: `EAGER` por defecto (carga automática).
- **Implementación en el proyecto**: `Venta` contiene `unCliente` con `@OneToOne` y `@JoinColumn`.

### @OneToMany (Uno a Muchos) – Venta ↔ Productos
Una entidad `Venta` se asocia con **múltiples** entidades `Producto` (una colección). Cada producto pertenece a una venta.
- **Ubicación del FK**: **Siempre** en la tabla del lado "Muchos" (`Producto`).
- **Dueño de la relación**: El lado con `@ManyToOne`.
- **Fetch Strategy**: `LAZY` por defecto (carga bajo demanda).
- **Implementación en el proyecto**: `Venta` expone `listaProductos` con `@OneToMany(mappedBy = "venta")`.

**Manejo de ciclos JSON**: Las anotaciones `@JsonManagedReference` (lado padre) y `@JsonBackReference` (lado hijo) previenen bucles infinitos en la serialización.

---
**Verificar:** Una vez levantado, accede a la documentación interactiva en:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---
## Endpoints principales

**Base URL:** `http://localhost:8080`

### 👤 Clientes (`/clientes`)
- `POST /clientes/crear` – Crear cliente  
- `GET /clientes` – Listar clientes  
- `GET /clientes/{id_cliente}` – Obtener cliente por ID  
- `PUT /clientes/editar/{id_cliente}` – Editar cliente  
- `DELETE /clientes/eliminar/{id_cliente}` – Eliminar cliente  

### 📦 Productos (`/productos`)
- `POST /productos/crear` – Crear producto  
- `GET /productos` – Listar productos  
- `GET /productos/{codigo_producto}` – Obtener producto por código  
- `PUT /productos/editar/{codigo_producto}` – Editar producto  
- `DELETE /productos/eliminar/{codigo_producto}` – Eliminar producto  
- `GET /productos/falta_stock` – Productos con bajo stock (cantidad \< 5)  

### 🧾 Ventas (`/ventas`)
- `POST /ventas/crear` – Crear venta (asocia productos existentes y un cliente existente)  
- `GET /ventas` – Listar ventas  
- `GET /ventas/{Id}` – Obtener venta por ID  
- `PUT /ventas/editar/{Id}` – Editar venta (productos y cliente)  
- `DELETE /ventas/eliminar/{Id}` – Eliminar venta  
- `GET /ventas/productos/{codigo_venta}` – Productos de una venta  
- `GET /ventas/montos/{fecha_venta}` – Sumatoria de montos y cantidad de ventas por fecha (`YYYY-MM-DD`)  
- `GET /ventas/mayor_venta` – Venta con mayor monto (`VentaMayorDTO`)  

> Los endpoints y ejemplos se basan en la colección Postman **Proyecto-tienda** incluida en el repo.

---

## ▶️ Ejemplos de uso (cURL)

### Crear Cliente
```bash
curl -X POST http://localhost:8080/clientes/crear   -H "Content-Type: application/json"   -d '{
    "nombre": "María",
    "apellido": "Cabrera",
    "dni": "0923456789"
  }'
```

### Crear Producto
```bash
curl -X POST http://localhost:8080/productos/crear   -H "Content-Type: application/json"   -d '{
    "nombre": "Mouse Logitech M185",
    "marca": "Logitech",
    "costo": 16.99,
    "cantidad_disponible": 75
  }'
```

### Crear Venta (referenciando IDs existentes)
```bash
curl -X POST http://localhost:8080/ventas/crear   -H "Content-Type: application/json"   -d '{
    "fecha_venta": "2025-10-07",
    "total": 950.0,
    "listaProductos": [
      { "codigo_producto": 1 }
    ],
    "unCliente": { "id_cliente": 1 }
  }'
```

### Obtener venta con mayor monto
```bash
curl http://localhost:8080/ventas/mayor_venta
```
Respuesta (ejemplo):
```json
{
  "codigo_venta": 252,
  "total": 950.0,
  "cantidad_productos": 2,
  "nombre_cliente": "Lucía",
  "apellido_cliente": "Paredes"
}
```

### Productos con bajo stock
```bash
curl http://localhost:8080/productos/falta_stock
```

### Resumen diario (monto total y #ventas)
```bash
curl http://localhost:8080/ventas/montos/2025-10-07
```
Respuesta (ejemplo):
```
La sumatoriaMonto: 1470.0, y el numero de ventas: 3
```

---

## 🧪 Colección Postman

Se incluye **`Proyecto-tienda.postman_collection.json`** con requests para todos los endpoints (crear/listar/editar/eliminar de clientes, productos y ventas, así como consultas agregadas). Importa la colección en Postman y define la variable `{{URL}}` con el host del servicio (por ejemplo, `http://localhost:8080`).

---

## 🚀 Ejecución local

1. **Requisitos**: Java 21 (recomendado), Maven/Gradle, Docker (opcional si deseas DB externa).  
2. **Configura la base de datos** en `src/main/resources/application.properties` (perfil por defecto) o usa memoria si está configurado.  
3. **Compilar y ejecutar**:
   ```bash
   ./mvnw spring-boot:run
   # o
   ./gradlew bootRun
   ```
4. El servicio escuchará en `http://localhost:8080`.

---

## ✅ Buenas prácticas recomendadas
- Uso de **DTO** para respuestas agregadas (`VentaMayorDTO`).  
- Relaciones mapeadas con JPA y control de ciclos JSON con Jackson.  
- Endpoint para **bajo stock** (ejemplo de lógica de dominio simple en controlador/servicio).

---
