# Auth Service

Microservicio responsable de la autenticación y gestión de usuarios en el ecosistema **CoopCredit**. Implementa **Arquitectura Hexagonal**.

## 📋 Características

*   Registro de usuarios.
*   Inicio de sesión y generación de tokens **JWT**.
*   Validación de usuarios existentes.
*   Integración con **Eureka Client** para descubrimiento.

## 🏗 Arquitectura

La estructura de paquetes sigue estrictamente la Arquitectura Hexagonal:

*   `domain`: Modelos (`User`) y Puertos (`UserRepository`, `LoginUserUseCase`).
*   `application`: Servicios de aplicación (`LoginUserService`, `RegisterUserService`).
*   `infrastructure`:
    *   `controllers`: Controladores REST (`AuthController`) y Manejo de Excepciones.
    *   `persistence`: Entidades JPA y Repositorios.
    *   `security`: Configuración de Spring Security y JWT.

## 🔌 API Endpoints

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Registrar un nuevo usuario |
| `POST` | `/api/auth/login` | Iniciar sesión y obtener JWT |
| `GET` | `/api/auth/users/{id}` | Obtener detalles de usuario (Interno) |

## ⚙️ Configuración

El servicio se configura mediante `application.yml` y variables de entorno inyectadas por Docker Compose:

*   `SPRING_DATASOURCE_URL`: Conexión a `coopcredit_auth`.
*   `JWT_SECRET`: Clave para firmar tokens.
*   `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`: URL del servidor Eureka.
