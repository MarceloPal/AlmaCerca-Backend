## Documentación de la API

A continuación se detallan los endpoints disponibles en el Backend.

> **Nota de Seguridad:** Para las rutas protegidas (ADMIN y BUYER), es obligatorio enviar el Header: `userId: [ID_DEL_USUARIO]`.

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

| Método | Endpoint | Descripción | Body (JSON) |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/cart` | Muestra los ítems activos del carrito del usuario. | - |
| `POST` | `/api/cart/add` | Agrega o incrementa un ítem al carrito activo. | `{"productId": "...", "quantity": 1}` |
| `GET` | `/api/cart/items/{productId}` | Obtiene los detalles de un ítem específico. | - |
| `PUT` | `/api/cart/items/{productId}` | Actualiza la cantidad de un ítem existente en el carrito. | `{"quantity": 5}` |
| `DELETE` | `/api/cart/items/{productId}` | Elimina un ítem específico del carrito activo. | - |
| `POST` | `/api/cart/buy` | Finaliza la compra y marca los ítems como `PURCHASED` (Borrado Lógico). | - |

---

## Registro de Mitigación de Vulnerabilidades Críticas

### 1. Síntesis del Incidente (RCA)

Se identificaron Vulnerabilidades Críticas (P1) en las versiones de dependencias.

### 2. Estrategia de Mitigación (HOTFIX)

La mitigación se aplicó bajo el principio de Mínima Dependencia Requerida.

#### 2.1 Actualización Crítica de Componentes
* `spring-boot-starter-parent` fue actualizado de la versión `3.2.5` a la versión `3.3.6`.
* Se removieron dependencias no utilizadas como `com.mysql:mysql-connector-j` y `io.jsonwebtoken:*` para reducir la superficie de ataque.

#### 2.2 Validación de Seguridad

* **Error 403 (Forbidden):** Se resolvió al agregar `.requestMatchers("/api/cart/**", "/api/admin/**").permitAll()` en `SecurityConfig`.

### 3. Resultado Final

* **Resultado del Escaneo:** 0 Vulnerabilidades Críticas después del reanálisis.
* **Conclusión:** La línea base queda segura, estabilizada y con deuda técnica de seguridad resuelta.