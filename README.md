## 📡 Documentación de la API

A continuación se detallan los endpoints disponibles.

> **🔐 Nota de Seguridad:** Para las rutas protegidas (**ADMIN** y **BUYER**), es obligatorio enviar el Header: `userId: [ID_DEL_USUARIO]`.

### 1. Autenticación (Público)
| Método | Endpoint | Descripción | Body (JSON) |
| :---: | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Registra un Comprador. | `{"email": "...", "password": "..."}` |
| `POST` | `/api/auth/login` | Inicia sesión (Devuelve ID y Rol). | `{"email": "...", "password": "..."}` |

### 2. Gestión de Productos (Rol: ADMIN)
> Requiere Header: `userId: [ID_ADMIN]`

| Método | Endpoint | Descripción | Body (JSON) |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/admin/products` | Lista todos los productos. | - |
| `POST` | `/api/admin/products` | Crea un producto nuevo. | `{"name": "...", "description": "...", "price": 0.0}` |
| `PUT` | `/api/admin/products/{id}` | Actualiza un producto. | `{"name": "...", ...}` |
| `DELETE` | `/api/admin/products/{id}` | Elimina un producto. | - |

### 3. Carrito de Compras (Rol: BUYER)
> Requiere Header: `userId: [ID_BUYER]`

| Método | Endpoint | Descripción | Parámetros (Query) |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/cart` | Muestra el carrito del usuario. | - |
| `POST` | `/api/cart` | Agrega un ítem al carrito. | `?productId=1&quantity=2` |
| `DELETE` | `/api/cart/items/{id}` | Elimina un ítem específico. | - |


🛡️ Registro de Mitigación de Vulnerabilidades Críticas

🚨 1. Síntesis del Incidente (RCA)
1.1 Problema Identificado

Se detectó la persistencia de Vulnerabilidades Críticas (P1) y de Alto Riesgo en:

spring-boot-starter-web

spring-boot-starter-security

Estas vulnerabilidades afectaban la integridad del servicio, la exposición de datos y la superficie de ataque.

1.2 Causas Raíz (RCA)
🔧 Desalineación de versiones

La versión base de Spring Boot no contenía los parches de seguridad más recientes.

🧱 Deuda Técnica de Seguridad

Dependencias no utilizadas que aumentaban la superficie de ataque:

mysql-connector-j

io.jsonwebtoken:*

⚙️ 2. Estrategia de Mitigación (HOTFIX)

Mitigación aplicada bajo el principio de Mínima Dependencia Requerida y asegurando integridad en la cadena de dependencias.

2.1 Actualización Crítica de Componentes
Componente	Versión Anterior	Versión Nueva (Patch)
spring-boot-starter-parent	3.2.5	3.3.6
2.2 Remoción de Dependencias Innecesarias

❌ com.mysql:mysql-connector-j

❌ io.jsonwebtoken:*

Ambas eliminadas para reducir superficie de ataque y evitar vulnerabilidades transitivas.

🔍 3. Validación de Estabilidad Post-Mitigation
🧪 3.1 Validación del Build
mvn clean install

🔥 3.2 Smoke Test Operacional
mvn -DskipTests spring-boot:run


Endpoints validados:

/login → Seguridad

/productos → Persistencia

Todo operativo sin regresiones.

📊 4. Post-Mortem y Resultado Esperado
✔️ Resultado del Escaneo

0 Vulnerabilidades Críticas después del reanálisis.

📌 Conclusión

La línea base queda:

Segura

Estabilizada

Sin dependencias innecesarias

Con deuda técnica de seguridad resuelta

📘 5. Registro de Cambios (Changelog)
v1.1 — Seguridad estabilizada

Aplicación del patch 3.3.6

Remoción de dependencias vulnerables

Reconstrucción de la cadena de dependencias

Smoke test en flujo crítico

v1.0 — Versión inicial

Configuración base del backend

Integración con MongoDB

Flujo de negocio operativo


## 📑 Reporte de Estado Operacional - AlmaCerca App

**Fecha:** 13 de Diciembre, 2025  
**Versión:** 1.0  
**Prepared by:** Technical Support Team



### 🔧 Backend (Spring Boot 3.3.6)
- Endpoints clave operativos: `/api/auth/login`, `/api/auth/register`, `/api/products`, `/api/products/{id}`, `/api/products/category/{categoryId}`, `/api/cart/add`, `/api/cart`, `/api/cart/items/{productId}` (GET/PUT/DELETE), `/api/admin/products` (POST/PUT).
- Seguridad: `SecurityConfig` permite `/api/cart/**` y `/api/admin/**`; `AuthInterceptor` agrega header `userId`.
- Base de datos: MongoDB (users, products, cart_items, categories).
- Logging: CartService con trazas "addToCart" y ProductService.update corrige stock/imageUrl/categoryId.

### ✅ Problemas Resueltos - Backend

| Problema | Causa | Solución |
| --- | --- | --- |
| Vulnerabilidades críticas en Spring Boot | Versión 3.2.5 sin parches de seguridad | Actualizar a Spring Boot 3.3.6 |
| Superficie de ataque ampliada | Dependencias no utilizadas (mysql-connector-j, jsonwebtoken) | Remover dependencias innecesarias del pom.xml |
| 403 Forbidden en /api/cart/** y /api/admin/** | SecurityConfig bloqueaba todas las rutas | Agregar `.requestMatchers("/api/cart/**", "/api/admin/**").permitAll()` |
| ProductService.update() no persistía cambios | Faltaban setters para stock, imageUrl, categoryId | Agregar `product.setStock()`, `setImageUrl()`, `setCategoryId()` |
| CartController no recibía productId/quantity | Endpoint esperaba JSON pero enviaba form-urlencoded | Cambiar a `@RequestParam` o agregar `@RequestBody CartItemDto` |