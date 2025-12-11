# CoopCredit Microservices Architecture

Este repositorio contiene la implementación de la arquitectura de microservicios para el sistema **CoopCredit**. El proyecto está estructurado como un monorepo y utiliza **Spring Boot**, **Spring Cloud Netflix Eureka**, y **PostgreSQL** orquestados mediante **Docker Compose**.

## 🏗 Arquitectura

El sistema sigue una **Arquitectura Hexagonal (Puertos y Adaptadores)** estricta para desacoplar la lógica de negocio de la infraestructura.

### Componentes Principales

*   **Eureka Server (`eureka-server`)**: Servidor de descubrimiento de servicios. Permite que los microservicios se registren y se encuentren entre sí dinámicamente.
*   **Auth Service (`auth-service`)**: Microservicio encargado de la gestión de usuarios y autenticación mediante **JWT**.
*   **Solicitudes Service (`solicitudes-service`)**: Microservicio para la gestión de solicitudes de crédito. Se comunica con `auth-service` para validar usuarios.
*   **PostgreSQL (`postgres-microservices-coopcredit`)**: Instancia única de base de datos compartida que aloja bases de datos independientes para cada servicio (`coopcredit_auth`, `coopcredit_solicitudes`).

## 🚀 Tecnologías

*   **Java 17**
*   **Spring Boot 3.2.0**
*   **Spring Cloud 2023.0.0** (Eureka, LoadBalancer)
*   **PostgreSQL 17 (Alpine)**
*   **Docker & Docker Compose** (Multi-stage builds)
*   **Maven**

## 📂 Estructura del Proyecto

```
micro-services/
├── docker-compose.yml      # Orquestación de contenedores
├── .env                    # Variables de entorno (Secretos)
├── pom.xml                 # Configuración Maven padre
├── db/init/                # Scripts SQL de inicialización
├── eureka-server/          # Servidor de descubrimiento
├── auth-service/           # Servicio de Autenticación
└── solicitudes-service/    # Servicio de Solicitudes
```

## 🛠 Configuración y Ejecución

### Prerrequisitos

*   Docker y Docker Compose instalados.

### Pasos para Ejecutar

1.  **Configurar Variables de Entorno**:
    Asegúrese de tener el archivo `.env` en la raíz con las siguientes variables:
    ```properties
    JWT_SECRET=su_clave_secreta_muy_segura_y_larga
    JWT_EXPIRATION=86400000
    ```

2.  **Levantar la Infraestructura**:
    No es necesario compilar localmente. Docker se encarga de todo el proceso de construcción (Multi-stage build).
    ```bash
    docker-compose up -d --build
    ```

3.  **Verificar Servicios**:
    *   **Eureka Dashboard**: [http://localhost:8761](http://localhost:8761)
    *   **Auth Service Health**: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)
    *   **Solicitudes Service Health**: [http://localhost:8082/actuator/health](http://localhost:8082/actuator/health)

## 📐 Detalles de Arquitectura Hexagonal

Para cumplir con los principios de diseño limpio:
*   **Dominio**: Contiene modelos y puertos (interfaces). No tiene dependencias de frameworks.
*   **Aplicación**: Contiene casos de uso y servicios de aplicación.
*   **Infraestructura**: Contiene la implementación de adaptadores (persistencia, controladores REST, clientes externos).
    *   **Controllers**: Ubicados en `infrastructure/controllers`.
    *   **Exceptions**: Ubicadas en `infrastructure/controllers/exception`.

---
**CoopCredit Team** - 2025
