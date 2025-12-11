# Solicitudes Service

Microservicio encargado de la gestión del ciclo de vida de las solicitudes de crédito. Se comunica con `auth-service` para validar la existencia de usuarios.

## 📋 Características

*   Creación de solicitudes de crédito.
*   Listado y consulta de solicitudes por usuario o rol.
*   Comunicación entre microservicios usando **WebClient** y **Spring Cloud LoadBalancer**.

## 🏗 Arquitectura

Implementa **Arquitectura Hexagonal** con separación estricta de capas:

*   `domain`: Modelos (`CreditApplication`) y Puertos (`UserPort`, `CreditApplicationRepository`).
*   `application`: Lógica de negocio (`CreditApplicationService`).
*   `infrastructure`:
    *   `controllers`: Controladores REST (`CreditApplicationController`) y Excepciones.
    *   `adapter/external`: Adaptador para `auth-service` (`UserAdapter`).
    *   `persistence`: Persistencia con JPA.

## 🔌 API Endpoints

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/credit-applications` | Crear solicitud |
| `GET` | `/api/credit-applications` | Listar solicitudes (según rol) |
| `GET` | `/api/credit-applications/{id}` | Obtener detalle de solicitud |

## 🤝 Comunicación entre Servicios

Este servicio utiliza `WebClient` configurado con `@LoadBalanced` para comunicarse con `auth-service` a través de Eureka:

```java
// UserAdapter.java
this.webClient = webClientBuilder.baseUrl("lb://AUTH-SERVICE").build();
```
