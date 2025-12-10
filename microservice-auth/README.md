# 🔐 Microservice Auth

## 📋 Descripción General

Microservicio de autenticación y gestión de usuarios para el sistema CoopCredit. Implementa autenticación mediante JWT (JSON Web Tokens) y gestión de usuarios con roles basados en permisos. Construido con **Arquitectura Hexagonal (Ports & Adapters)** para mantener la lógica de negocio desacoplada de los detalles de infraestructura.

---

## 🎯 Propósito

- Registro de nuevos usuarios con validación
- Autenticación de usuarios mediante email/password
- Generación y validación de tokens JWT
- Gestión de roles de usuario (AFILIADO, ANALISTA, ADMIN)
- Almacenamiento seguro de credenciales (BCrypt)
- Service Discovery con Eureka

---

## 🏗️ Arquitectura Hexagonal

```
microservice-auth/
├── adapter/               # Adaptadores (entrada/salida)
│   └── web/              # REST Controllers
├── application/          # Casos de uso
│   ├── dto/             # DTOs de entrada/salida
│   └── service/         # Implementación de casos de uso
├── domain/              # Lógica de negocio (núcleo)
│   ├── model/          # Entidades del dominio
│   └── port/           # Interfaces (puertos)
│       ├── in/        # Puertos de entrada (use cases)
│       └── out/       # Puertos de salida (repository)
└── infrastructure/      # Implementaciones técnicas
    ├── config/         # Configuración Spring
    ├── persistence/    # JPA/PostgreSQL
    └── security/       # Spring Security + JWT
```

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 4.0.0 | Framework principal |
| Spring Security | 4.0.0 | Autenticación y autorización |
| Spring Data JPA | 4.0.0 | Persistencia de datos |
| PostgreSQL | 42.7.3 | Base de datos |
| JJWT | 0.12.6 | Generación y validación JWT |
| Spring Cloud Eureka | 2022.0.3 | Service Discovery |
| Lombok | 1.18.30 | Reducción de boilerplate |
| BCrypt | (incluido) | Encriptación de contraseñas |

---

## 📡 API Endpoints

### Autenticación (Públicos)

#### Registro de Usuario
```http
POST /api/auth/register
Content-Type: application/json

{
  "document": "12345678",
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "password": "securePassword123",
  "salary": 5000.00
}
```

**Respuesta (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan@example.com",
  "name": "Juan Pérez",
  "role": "ROLE_AFILIADO",
  "userId": 1
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "juan@example.com",
  "password": "securePassword123"
}
```

**Respuesta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan@example.com",
  "name": "Juan Pérez",
  "role": "ROLE_AFILIADO",
  "userId": 1
}
```

### Gestión de Usuarios (Protegidos)

#### Obtener Usuario por ID
```http
GET /api/auth/users/{id}
Authorization: Bearer {token}
```

**Respuesta (200 OK):**
```json
{
  "token": null,
  "email": "juan@example.com",
  "name": "Juan Pérez",
  "role": "ROLE_AFILIADO",
  "userId": 1
}
```

#### Health Check
```http
GET /api/auth/health
```

**Respuesta (200 OK):**
```
Auth service is running
```

---

## 🔑 Roles de Usuario

| Rol | Descripción | Permisos |
|-----|-------------|----------|
| **ROLE_AFILIADO** | Usuario afiliado | Crear solicitudes propias, ver propias |
| **ROLE_ANALISTA** | Analista de crédito | CRUD completo de solicitudes |
| **ROLE_ADMIN** | Administrador | Control total del sistema |

---

## ⚙️ Configuración

### Variables de Entorno / application.yml

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/mi_base
    username: admin
    password: admin123
  
  jpa:
    hibernate:
      ddl-auto: update  # Cambia a 'validate' en producción
    show-sql: true      # Desactiva en producción

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

jwt:
  secret: CoopCreditSecretKeyForJWTTokenGeneration2024VerySecureKeyAtLeast256Bits
  expiration: 86400000  # 24 horas
```

---

## 🚀 Instalación y Ejecución

### Prerequisitos
- Java 17+
- Maven 3.8+
- PostgreSQL 15+ (puerto 5433)

### 1. Levantar Base de Datos

#### Con Docker Compose:
```bash
cd microservice-auth
docker-compose up -d
```

#### Manual:
```sql
CREATE DATABASE mi_base;
CREATE USER admin WITH PASSWORD 'admin123';
GRANT ALL PRIVILEGES ON DATABASE mi_base TO admin;
```

### 2. Compilar
```bash
mvn clean install -DskipTests
```

### 3. Ejecutar
```bash
mvn spring-boot:run
```

O como JAR:
```bash
mvn package
java -jar target/microservice-auth-1.0-SNAPSHOT.jar
```

### 4. Verificar
```bash
curl http://localhost:8081/api/auth/health
```

---

## 🔐 Seguridad

### JWT Token Structure
```
Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "sub": "email@example.com",
  "userId": 1,
  "role": "ROLE_AFILIADO",
  "iat": 1702310400,
  "exp": 1702396800
}
```

### Configuración de Seguridad
- **Algoritmo:** HS256 (HMAC-SHA256)
- **Secret:** 256+ bits
- **Expiración:** 24 horas
- **Encriptación de Passwords:** BCrypt (strength 10)

### Endpoints Públicos
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/health`

### Endpoints Protegidos
- `GET /api/auth/users/{id}` - Requiere JWT válido

---

## 🧪 Testing

### Pruebas Automatizadas
```bash
mvn test
```

### Pruebas Manuales con cURL

**Registrar usuario:**
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "document": "12345678",
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "salary": 3000.00
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Obtener usuario (con token):**
```bash
TOKEN="tu_token_aqui"
curl -X GET http://localhost:8081/api/auth/users/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 📊 Modelo de Datos

### Entidad User

| Campo | Tipo | Descripción | Constraints |
|-------|------|-------------|-------------|
| id | Long | ID autogenerado | PRIMARY KEY |
| document | String | Documento de identidad | UNIQUE, NOT NULL |
| name | String | Nombre completo | NOT NULL |
| email | String | Email | UNIQUE, NOT NULL |
| password | String | Password encriptado | NOT NULL |
| salary | BigDecimal | Salario | NOT NULL |
| role | Enum | Rol del usuario | NOT NULL, DEFAULT: AFILIADO |
| createdAt | LocalDateTime | Fecha de creación | AUTO |
| updatedAt | LocalDateTime | Fecha de actualización | AUTO |

---

## 🐳 Docker

### Dockerfile (ejemplo)
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/microservice-auth-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build y Run
```bash
docker build -t microservice-auth:1.0 .
docker run -p 8081:8081 microservice-auth:1.0
```

---

## 📝 Logs

Los logs están configurados en nivel DEBUG para desarrollo:
```yaml
logging:
  level:
    com.mycompany.microservice.auth: DEBUG
    org.springframework.security: DEBUG
```

**Cambiar a INFO/WARN en producción.**

---

## 🔧 Troubleshooting

### Error: "Could not connect to database"
- Verificar que PostgreSQL esté corriendo en puerto 5433
- Verificar credenciales (admin/admin123)
- Verificar que la base de datos `mi_base` exista

### Error: "JWT signature does not match"
- Verificar que el secret JWT sea el mismo en todos los servicios
- Verificar que el token no haya expirado

### Error: "Port 8081 already in use"
```bash
lsof -i :8081
kill -9 <PID>
```

---

## 📚 Recursos Adicionales

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JJWT Documentation](https://github.com/jwtk/jjwt)
- [Hexagonal Architecture](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)

---

## 👥 Equipo de Desarrollo

**Mantenedor:** CoopCredit Development Team  
**Versión:** 1.0-SNAPSHOT  
**Última actualización:** Diciembre 2025
