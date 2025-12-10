# 🏦 CoopCredit - Sistema de Solicitudes de Crédito

Sistema de microservicios para gestión de solicitudes de crédito cooperativo con arquitectura hexagonal.

## 🏗️ Arquitectura

```
CoopCredit/
├── microservice-eureka-server/     # Service Discovery (Puerto 8761)
├── microservice-auth/              # Autenticación y Usuarios (Puerto 8081)
├── microservice-credit-application/# Gestión de Solicitudes (Puerto 8082)
├── risk-central-mock-service/      # Evaluación de Riesgo Mock (Puerto 8083)
└── docker-compose.yml             # PostgreSQL compartida (Puerto 5433)
```

## 🗄️ Base de Datos

**PostgreSQL Compartida** en puerto **5433** con dos bases de datos:

| Base de Datos | Servicio | Tablas |
|---------------|----------|--------|
| `mi_base` | Auth | `users` |
| `solicitudes` | Credit | `credit_applications` |

**Credenciales:**
- Usuario: `admin`
- Password: `admin123`

## 🚀 Inicio Rápido

### 1. Iniciar PostgreSQL
```bash
docker-compose up -d
```

### 2. Iniciar Servicios (en orden)

```bash
# 1. Eureka Server
cd microservice-eureka-server
mvn spring-boot:run

# 2. Auth Service
cd microservice-auth
mvn spring-boot:run

# 3. Credit Service
cd microservice-credit-application
mvn spring-boot:run

# 4. Risk Service
cd risk-central-mock-service
mvn spring-boot:run
```

## 📡 Endpoints Principales

### Auth Service (8081)
- `POST /api/auth/register` - Registrar usuario
- `POST /api/auth/login` - Autenticación
- `GET /api/auth/health` - Health check

### Credit Service (8082)
- `POST /api/applications` - Crear solicitud
- `GET /api/applications/my` - Mis solicitudes
- `GET /api/applications/{id}` - Consultar por ID
- `GET /api/applications/health` - Health check

### Risk Service (8083)
- `POST /risk-evaluation` - Evaluar riesgo crediticio

### Eureka Dashboard
- `http://localhost:8761` - Dashboard de servicios

## 🧪 Pruebas con Postman

Importa la colección: `CoopCredit.postman_collection.json`

### Flujo de Prueba Completo

1. **Registrar Usuario:**
```json
POST http://localhost:8081/api/auth/register
{
  "document": "12345678",
  "name": "Juan Pérez",
  "email": "juan@test.com",
  "password": "password123",
  "salary": 5000.00
}
```

2. **Crear Solicitud:**
```json
POST http://localhost:8082/api/applications
Authorization: Bearer {token}
{
  "amount": 250000.00,
  "termMonths": 36,
  "purpose": "Compra de maquinaria"
}
```

3. **Evaluar Riesgo:**
```json
POST http://localhost:8083/risk-evaluation
{
  "documento": "12345678",
  "monto": 100000,
  "plazo": 12
}
```

## 🛠️ Tecnologías

- **Spring Boot** 3.2.0
- **Spring Cloud** 2023.0.0
- **PostgreSQL** 15
- **Eureka** Service Discovery
- **JWT** para autenticación
- **Maven** para gestión de dependencias

## 📋 Requisitos

- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL (vía Docker)

## 🔐 Roles y Permisos

| Rol | Permisos |
|-----|----------|
| **AFILIADO** | Ver y crear sus propias solicitudes |
| **ANALISTA** | Ver y actualizar todas las solicitudes |
| **ADMIN** | Acceso completo (CRUD) |

## 📊 Estado del Sistema

✅ **Funcionando 100%:**
- Autenticación JWT
- Registro de usuarios
- Creación de solicitudes
- Consulta de solicitudes
- Evaluación de riesgo
- Service Discovery
- PostgreSQL compartida

## 📖 Documentación Adicional

- Ver READMEs individuales en cada microservicio
- Colección de Postman incluida
- Guía de ejecución en `/docs`

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es de código abierto.
