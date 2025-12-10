# 💳 Credit Application Service - Gestión de Solicitudes de Crédito

Microservicio para gestión de solicitudes de crédito con control de acceso basado en roles.

## 🎯 Características

- Creación de solicitudes de crédito
- Consulta de solicitudes propias
- Control de acceso por roles (AFILIADO, ANALISTA, ADMIN)
- Validación de datos
- Integración con Auth Service (JWT)
- Persistencia en PostgreSQL

## 🗄️ Base de Datos

**PostgreSQL** (puerto 5433)
- Base de datos: `solicitudes`
- Usuario: `admin`
- Password: `admin123`

### Tabla: credit_applications
```sql
CREATE TABLE credit_applications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    term_months INTEGER NOT NULL,
    purpose VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    analyst_notes TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## 📡 Endpoints

### Crear Solicitud (AFILIADO)
```
POST /api/applications
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 250000.00,
  "termMonths": 36,
  "purpose": "Compra de maquinaria"
}
```

**Response:**
```json
{
  "id": 1,
  "userId": 5,
  "amount": 250000.00,
  "termMonths": 36,
  "purpose": "Compra de maquinaria",
  "status": "PENDIENTE",
  "statusDescription": "Solicitud pendiente de revisión",
  "createdAt": "2025-12-10T15:47:42"
}
```

### Consultar Mis Solicitudes (AFILIADO)
```
GET /api/applications/my
Authorization: Bearer {token}
```

### Consultar por ID
```
GET /api/applications/{id}
Authorization: Bearer {token}
```

### Actualizar Solicitud (ANALISTA/ADMIN)
```
PUT /api/applications/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 300000.00,
  "termMonths": 48,
  "purpose": "Ampliación de negocio"
}
```

### Eliminar Solicitud
```
DELETE /api/applications/{id}
Authorization: Bearer {token}
```

### Health Check
```
GET /api/applications/health
```

## 🚀 Ejecución

```bash
# Iniciar PostgreSQL primero
cd ../
docker-compose up -d

# Ejecutar servicio
mvn spring-boot:run
```

**Puerto:** 8082

## 🔧 Configuración

`application.yml`:
```yaml
server:
  port: 8082

spring:
  application:
    name: microservice-credit-application
  
  datasource:
    url: jdbc:postgresql://localhost:5433/solicitudes
    username: admin
    password: admin123
    driver-class-name: org.postgresql.Driver

jwt:
  secret: CoopCreditSecretKeyForJWTTokenGeneration2024...

auth:
  service:
    url: http://localhost:8081
```

## 🔐 Roles y Permisos

| Rol | Crear | Ver Propias | Ver Todas | Actualizar | Eliminar |
|-----|-------|-------------|-----------|------------|----------|
| **AFILIADO** | ✅ | ✅ | ❌ | ❌ | ✅ (solo PENDING) |
| **ANALISTA** | ✅ | ✅ | ✅ | ✅ | ✅ |
| **ADMIN** | ✅ | ✅ | ✅ | ✅ | ✅ |

## 📊 Estados de Solicitud

- **PENDIENTE** - Solicitud recién creada
- **EN_REVISION** - En proceso de análisis
- **APROBADA** - Solicitud aprobada
- **RECHAZADA** - Solicitud rechazada
- **CANCELADA** - Cancelada por el usuario

## 🏗️ Arquitectura

```
adapter/
  ├── web/          # Controllers REST
infrastructure/
  ├── persistence/  # JPA Repositories
  ├── security/     # JWT Validation
  ├── adapter/      # External services
domain/
  ├── model/        # Entidades
  ├── port/         # Interfaces
  ├── exception/    # Excepciones
application/
  ├── service/      # Servicios
  ├── dto/          # DTOs
```

## 📦 Validaciones

- **amount:** Mínimo 100,000
- **termMonths:** Entre 1 y 120 meses
- **purpose:** Máximo 500 caracteres

## ✅ Estado

- ✅ Creación funcionando
- ✅ Consultas funcionando
- ✅ PostgreSQL conectado
- ✅ JWT validación OK
- ✅ Eureka registrado
- ✅ Control de acceso funcionando
