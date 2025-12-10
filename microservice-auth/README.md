# 🔐 Auth Service - Microservicio de Autenticación

Microservicio de autenticación y gestión de usuarios con arquitectura hexagonal y JWT.

## 🎯 Características

- Registro de usuarios con validación
- Autenticación JWT
- Gestión de roles (AFILIADO, ANALISTA, ADMIN)
- Encriptación BCrypt de contraseñas
- Integración con Eureka
- Persistencia en PostgreSQL

## 🗄️ Base de Datos

**PostgreSQL** (puerto 5433)
- Base de datos: `mi_base`
- Usuario: `admin`
- Password: `admin123`

### Tabla: users
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    salary DECIMAL(15,2),
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20),
    affiliation_date DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## 📡 Endpoints

### Registrar Usuario
```
POST /api/auth/register
Content-Type: application/json

{
  "document": "12345678",
  "name": "Juan Pérez",
  "email": "juan@test.com",
  "password": "password123",
  "salary": 5000.00
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "email": "juan@test.com",
  "name": "Juan Pérez",
  "role": "ROLE_AFILIADO",
  "userId": 1,
  "message": "User registered successfully"
}
```

### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "juan@test.com",
  "password": "password123"
}
```

### Health Check
```
GET /api/auth/health
```

## 🚀 Ejecución

```bash
# Iniciar PostgreSQL primero
cd ../
docker-compose up -d

# Ejecutar servicio
mvn spring-boot:run
```

**Puerto:** 8081

## 🔧 Configuración

`application.yml`:
```yaml
server:
  port: 8081

spring:
  application:
    name: microservice-auth
  
  datasource:
    url: jdbc:postgresql://localhost:5433/mi_base
    username: admin
    password: admin123
    driver-class-name: org.postgresql.Driver

jwt:
  secret: CoopCreditSecretKeyForJWTTokenGeneration2024...
  expiration: 86400000  # 24 horas
```

## 🏗️ Arquitectura Hexagonal

```
adapter/
  ├── web/          # Controllers REST
infrastructure/
  ├── persistence/  # JPA Repositories
  ├── security/     # JWT, Spring Security
domain/
  ├── model/        # Entidades de dominio
  ├── port/         # Interfaces (in/out)
application/
  ├── usecase/      # Casos de uso
  ├── dto/          # DTOs
```

## 🔐 JWT

- **Algoritmo:** HS512
- **Expiración:** 24 horas
- **Claims:** userId, email, role

## 📦 Dependencias Principales

- Spring Boot Web
- Spring Security
- Spring Data JPA
- PostgreSQL Driver
- JJWT (JWT)
- Eureka Client
- Lombok

## ✅ Estado

- ✅ Registro funcionando
- ✅ Login funcionando
- ✅ JWT funcionando
- ✅ PostgreSQL conectado
- ✅ Eureka registrado
