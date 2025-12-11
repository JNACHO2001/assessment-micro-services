# Eureka Server

Servidor de descubrimiento de servicios (Service Discovery) basado en **Spring Cloud Netflix Eureka**. Actúa como el registro central donde todos los microservicios se anuncian y se localizan entre sí.

## 🚀 Funcionalidad

*   **Registro**: Los microservicios (`auth-service`, `solicitudes-service`) se registran automáticamente al iniciar.
*   **Descubrimiento**: Permite a los clientes resolver nombres lógicos de servicio (ej. `AUTH-SERVICE`) a direcciones IP y puertos concretos.
*   **Monitoreo**: Proporciona un dashboard web para visualizar el estado de los servicios registrados.

## 🛠 Configuración

El servidor escucha en el puerto **8761** por defecto.

### Dashboard
Acceda al panel de control en: [http://localhost:8761](http://localhost:8761)

## 🐳 Despliegue

Utiliza un **Dockerfile Multi-stage**:
1.  **Build**: Compila el código usando Maven y JDK 17.
2.  **Run**: Ejecuta el JAR resultante en una imagen ligera JRE 17 Alpine.
