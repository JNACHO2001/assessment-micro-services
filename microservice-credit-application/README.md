# 💳 Microservice Credit Application

## 📋 Descripción General

Microservicio de gestión de solicitudes de crédito para el sistema CoopCredit. Gestiona el ciclo de vida completo de las solicitudes de crédito con control de acceso basado en roles, integración con el servicio de autenticación mediante JWT y evaluación de riesgo crediticio. Construido con **Arquitectura Hexagonal** para garantizar mantenibilidad y escalabilidad.

---

## 🎯 Propósito

- Crear y gestionar solicitudes de crédito
- Control de acceso basado en roles (AFILIADO, ANALISTA, ADMIN)
- Integración con servicio de autenticación (JWT validation)
- Integración con servicio de evaluación de riesgo
- Persistencia de solicitudes en PostgreSQL
- Auditoría de cambios de estado
- Service Discovery con Eureka

---

## 🏗️ Arquitectura Hexagonal

```
microservice-credit-application/
├── adapter/                    # Adaptadores
│   ├── web/                   # REST Controllers
│   └── external/              # Clientes externos
├── application/               # Capa de aplicación
│   ├── dto/                  # DTOs
│   └── service/              # Servicios de aplicación
├── domain/                    # Núcleo del negocio
│   ├── model/                # Entidades del dominio
│   └── repository/           # Interfaces de repositorio
└── infrastructure/            # Infraestructura técnica
    ├── config/               # Configuración Spring
    ├── persistence/          # JPA/PostgreSQL
    └── security/             # JWT validation
```

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 4.0.0 | Framework principal |
| Spring Security | 4.0.0 | Validación JWT |
| Spring Data JPA | 4.0.0 | Persistencia de datos |
| Spring WebFlux | 4.0.0 | Cliente HTTP reactivo |
| PostgreSQL | 42.7.3 | Base de datos |
| JJWT | 0.12.6 | Validación de tokens JWT |
| Spring Cloud Eureka | 2022.0.3 | Service Discovery |
| Lombok | 1.18.30 | Reducción de boilerplate |

---

## 📡 API Endpoints

### Solicitudes de Crédito

#### Crear Solicitud
```http
POST /api/applications
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 10000.00,
  "termMonths": 12,
  "purpose": "Compra de vehículo"
}
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "userId": 1,
  "userName": "Juan Pérez",
  "amount": 10000.00,
  "termMonths": 12,
  "purpose": "Compra de vehículo",
  "status": "PENDING",
  "analystNotes": null,
  "createdAt": "2025-12-10T14:15:00",
  "updatedAt": "2025-12-10T14:15:00"
}
```

**Roles permitidos:** AFILIADO, ANALISTA, ADMIN

---

#### Mis Solicitudes
```http
GET /api/applications/my
Authorization: Bearer {token}
```

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "userName": "Juan Pérez",
    "amount": 10000.00,
    "termMonths": 12,
    "purpose": "Compra de vehículo",
    "status": "PENDING",
    "analystNotes": null,
    "createdAt": "2025-12-10T14:15:00",
    "updatedAt": "2025-12-10T14:15:00"
  }
]
```

**Roles permitidos:** AFILIADO, ANALISTA, ADMIN

---

#### Listar Todas las Solicitudes
```http
GET /api/applications
Authorization: Bearer {token}
```

**Respuesta (200 OK):** Array de solicitudes

**Roles permitidos:** ANALISTA, ADMIN

---

#### Obtener Solicitud por ID
```http
GET /api/applications/{id}
Authorization: Bearer {token}
```

**Respuesta (200 OK):** Objeto de solicitud

**Roles permitidos:** 
- AFILIADO: Solo sus propias solicitudes
- ANALISTA, ADMIN: Cualquier solicitud

---

#### Actualizar Solicitud
```http
PUT /api/applications/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "analystNotes": "Aprobado con condiciones especiales",
  "status": "APPROVED"
}
```

**Respuesta (200 OK):** Solicitud actualizada

**Roles permitidos:** ANALISTA, ADMIN

---

#### Actualizar Estado (Solo Estado)
```http
PATCH /api/applications/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "REJECTED"
}
```

**Respuesta (200 OK):** Solicitud actualizada

**Roles permitidos:** ADMIN

---

#### Eliminar Solicitud
```http
DELETE /api/applications/{id}
Authorization: Bearer {token}
```

**Respuesta (204 No Content)**

**Roles permitidos:** 
- AFILIADO: Solo sus propias solicitudes en estado PENDING
- ANALISTA, ADMIN: Cualquier solicitud

---

#### Health Check
```http
GET /api/applications/health
```

**Respuesta (200 OK):**
```
Credit Application service is running
```

---

## 🔐 Control de Acceso

### Matriz de Permisos

| Operación | AFILIADO | ANALISTA | ADMIN |
|-----------|----------|----------|-------|
| Crear solicitud | ✅ Propia | ✅ Cualquiera | ✅ Cualquiera |
| Ver mis solicitudes | ✅ | ✅ | ✅ |
| Ver todas | ❌ | ✅ | ✅ |
| Ver por ID | ✅ Propia | ✅ Cualquiera | ✅ Cualquiera |
| Actualizar | ❌ | ✅ | ✅ |
| Cambiar estado | ❌ | ❌ | ✅ |
| Eliminar | ✅ Propia (PENDING) | ✅ Cualquiera | ✅ Cualquiera |

---

## 📊 Estados de Solicitud

| Estado | Descripción |
|--------|-------------|
| **PENDING** | Solicitud creada, pendiente de revisión |
| **UNDER_REVIEW** | En proceso de análisis |
| **APPROVED** | Aprobada por analista |
| **REJECTED** | Rechazada |
| **CANCELLED** | Cancelada por el usuario |
| **DISBURSED** | Desembolsada |

---

## ⚙️ Configuración

### Variables de Entorno / application.yml

```yaml
server:
  port: 8082

spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/solicitudes
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

auth:
  service:
    url: http://localhost:8081  # URL del servicio de auth
```

---

## 🚀 Instalación y Ejecución

### Prerequisitos
- Java 17+
- Maven 3.8+
- PostgreSQL 15+ (puerto 5433)
- microservice-auth ejecutándose (puerto 8081)
- microservice-eureka-server ejecutándose (puerto 8761)

### 1. Levantar Base de Datos

#### Con Docker Compose:
```bash
cd microservice-credit-application
docker-compose up -d
```

#### Manual:
```sql
CREATE DATABASE solicitudes;
CREATE USER admin WITH PASSWORD 'admin123';
GRANT ALL PRIVILEGES ON DATABASE solicitudes TO admin;
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
java -jar target/microservice-credit-application-1.0-SNAPSHOT.jar
```

### 4. Verificar
```bash
curl http://localhost:8082/api/applications/health
```

---

## 🔗 Integraciones

### Servicio de Autenticación

**Endpoint utilizado:**
```
GET http://localhost:8081/api/auth/users/{userId}
```

**Propósito:** Obtener información del usuario para asociar con la solicitud.

### Servicio de Evaluación de Riesgo

**Endpoint utilizado:**
```
POST http://localhost:8083/risk-evaluation
```

**Propósito:** Evaluar el riesgo crediticio del solicitante.

**Request:**
```json
{
  "documento": "12345678",
  "monto": 10000.00,
  "plazo": 12
}
```

**Response:**
```json
{
  "documento": "12345678",
  "score": 750,
  "nivelRiesgo": "BAJO",
  "detalle": "Excelente historial crediticio."
}
```

---

## 📊 Modelo de Datos

### Entidad CreditApplication

| Campo | Tipo | Descripción | Constraints |
|-------|------|-------------|-------------|
| id | Long | ID autogenerado | PRIMARY KEY |
| userId | Long | ID del usuario solicitante | NOT NULL, FK |
| amount | BigDecimal | Monto solicitado | NOT NULL, > 0 |
| termMonths | Integer | Plazo en meses | NOT NULL, > 0 |
| purpose | String | Propósito del crédito | NOT NULL |
| status | Enum | Estado de la solicitud | NOT NULL, DEFAULT: PENDING |
| analystNotes | String | Notas del analista | NULLABLE |
| createdAt | LocalDateTime | Fecha de creación | AUTO |
| updatedAt | LocalDateTime | Fecha de actualización | AUTO |

---

## 🧪 Testing

### Pruebas con cURL

**1. Obtener un token JWT del servicio auth:**
```bash
TOKEN=$(curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}' \
  | jq -r '.token')
```

**2. Crear solicitud:**
```bash
curl -X POST http://localhost:8082/api/applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "amount": 15000.00,
    "termMonths": 24,
    "purpose": "Mejoras en el hogar"
  }'
```

**3. Ver mis solicitudes:**
```bash
curl -X GET http://localhost:8082/api/applications/my \
  -H "Authorization: Bearer $TOKEN"
```

**4. Actualizar solicitud (requiere rol ANALISTA):**
```bash
curl -X PUT http://localhost:8082/api/applications/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "analystNotes": "Aprobado",
    "status": "APPROVED"
  }'
```

---

## 🐳 Docker

### Dockerfile (ejemplo)
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/microservice-credit-application-1.0-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build y Run
```bash
docker build -t microservice-credit-application:1.0 .
docker run -p 8082:8082 microservice-credit-application:1.0
```

---

## 📝 Logs

```yaml
logging:
  level:
    com.mycompany.microservice.credit: DEBUG
    org.springframework.security: DEBUG
```

**Cambiar a INFO/WARN en producción.**

---

## 🔧 Troubleshooting

### Error: "Unauthorized" (401)
- Verificar que el token JWT sea válido
- Verificar que el secret JWT sea el mismo en auth service
- Verificar que el token no haya expirado

### Error: "Forbidden" (403)
- Verificar que el usuario tenga el rol adecuado
- El AFILIADO solo puede ver/eliminar sus propias solicitudes

### Error: "Could not connect to auth service"
- Verificar que microservice-auth esté ejecutándose en puerto 8081
- Verificar configuración `auth.service.url`

### Error: "Port 8082 already in use"
```bash
lsof -i :8082
kill -9 <PID>
```

---

## 📚 Recursos Adicionales

- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Hexagonal Architecture Pattern](https://alistair.cockburn.us/hexagonal-architecture/)

---

## 👥 Equipo de Desarrollo

**Mantenedor:** CoopCredit Development Team  
**Versión:** 1.0-SNAPSHOT  
**Última actualización:** Diciembre 2025
