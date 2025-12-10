# 🌐 Microservice Eureka Server

## 📋 Descripción General

Servidor de descubrimiento de servicios (Service Discovery) para el ecosistema de microservicios CoopCredit. Implementa **Netflix Eureka Server** para proporcionar registro, descubrimiento y monitoreo de todos los microservicios de la arquitectura.

---

## 🎯 Propósito

- **Service Registry:** Registro centralizado de microservicios
- **Service Discovery:** Descubrimiento automático de servicios
- **Health Monitoring:** Monitoreo de salud de servicios registrados
- **Load Balancing:** Soporte para balanceo de carga entre instancias
- **Dashboard Web:** Interfaz visual para monitorear el ecosistema
- **Failover:** Detección y manejo de fallos de servicios

---

## 🏗️ Arquitectura

```
Eureka Server (8761)
    │
    ├── microservice-auth (8081)
    │   └── Instancias: 1 (puede escalar)
    │
    ├── microservice-credit-application (8082)
    │   └── Instancias: 1 (puede escalar)
    │
    └── [Otros servicios futuros]
```

### Flujo de Registro
```
1. Microservicio inicia
2. Se registra en Eureka Server
3. Envía heartbeats cada 30 segundos
4. Eureka mantiene registro actualizado
5. Otros servicios pueden descubrirlo
```

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 4.0.0 | Framework principal |
| Spring Cloud Netflix Eureka Server | 2022.0.3 | Service Discovery |
| Maven | 3.8+ | Build tool |

---

## 🖥️ Dashboard Web

### Acceso
```
URL: http://localhost:8761
```

### Información Disponible

#### System Status
- Entorno (test, dev, prod)
- Data center info
- Tiempo de inicio

#### General Info
- Zona de disponibilidad
- Estado total de instancias

#### Instance Info
- Servicios registrados
- Estado de cada servicio (UP, DOWN, STARTING)
- Número de instancias por servicio
- Última renovación (heartbeat)
- URLs de cada instancia

#### Renews Threshold
- Renovaciones esperadas por minuto
- Renovaciones actuales por minuto

---

## ⚙️ Configuración

### application.yml

```yaml
server:
  port: 8761

spring:
  application:
    name: microservice-eureka-server

eureka:
  client:
    register-with-eureka: false  # No registrarse a sí mismo
    fetch-registry: false         # No obtener registry de otros
  server:
    wait-time-in-ms-when-sync-empty: 0  # No esperar en desarrollo
```

### Configuración para Producción

```yaml
eureka:
  server:
    enable-self-preservation: true     # Activar modo auto-preservación
    eviction-interval-timer-in-ms: 60000  # Intervalo de evicción
    renewal-percent-threshold: 0.85    # Threshold de renovación
  instance:
    hostname: eureka-server.production.com
```

---

## 🚀 Instalación y Ejecución

### Prerequisitos
- Java 17+
- Maven 3.8+

### 1. Compilar
```bash
cd microservice-eureka-server
mvn clean install -DskipTests
```

### 2. Ejecutar
```bash
mvn spring-boot:run
```

O como JAR:
```bash
mvn package
java -jar target/microservice-eureka-server-1.0-SNAPSHOT.jar
```

### 3. Verificar
Abrir navegador:
```
http://localhost:8761
```

Deberías ver el dashboard de Eureka.

---

## 📡 Endpoints

### Dashboard
```http
GET http://localhost:8761/
```
**Respuesta:** Interfaz web HTML

### Registro de Servicios (Programático)
```http
GET http://localhost:8761/eureka/apps
```
**Respuesta:** XML con todos los servicios registrados

```http
GET http://localhost:8761/eureka/apps/{appName}
```
**Respuesta:** XML con información del servicio específico

### Metadata del Servidor
```http
GET http://localhost:8761/actuator/info
```

---

## 🔗 Configuración de Clientes

### Configuración en microservicios para conectarse a Eureka

```yaml
spring:
  application:
    name: mi-microservicio  # Nombre que aparecerá en Eureka

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/  # URL del servidor Eureka
    register-with-eureka: true   # Registrarse en Eureka
    fetch-registry: true          # Obtener el registry
  instance:
    prefer-ip-address: true       # Usar IP en vez de hostname
    lease-renewal-interval-in-seconds: 30      # Intervalo de heartbeat
    lease-expiration-duration-in-seconds: 90   # Tiempo antes de considerar muerto
```

### Dependencia Maven para Clientes

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### Anotación en Clase Principal del Cliente

```java
@SpringBootApplication
@EnableDiscoveryClient  // o @EnableEurekaClient
public class MiMicroservicioApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiMicroservicioApplication.class, args);
    }
}
```

---

## 📊 Servicios Registrados

### Servicios Actuales en CoopCredit

| Servicio | Puerto | Instancias | Estado |
|----------|--------|------------|--------|
| **MICROSERVICE-AUTH** | 8081 | 1 | UP |
| **MICROSERVICE-CREDIT-APPLICATION** | 8082 | 1 | UP |

### Verificar Servicios Registrados

**Mediante Dashboard:**
- Abrir `http://localhost:8761`
- Ver sección "Instances currently registered with Eureka"

**Mediante API:**
```bash
curl http://localhost:8761/eureka/apps
```

---

## 🔧 Self-Preservation Mode

### ¿Qué es?
Mecanismo de protección que previene la eliminación masiva de servicios cuando hay problemas de red.

### Cuándo se Activa
Cuando Eureka recibe menos heartbeats de los esperados (< 85% threshold).

### Comportamiento
- Eureka NO elimina servicios del registry
- Aparece mensaje en dashboard: "EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP..."

### Desactivar (Solo Desarrollo)
```yaml
eureka:
  server:
    enable-self-preservation: false
```

**⚠️ NO desactivar en producción**

---

## 🐳 Docker

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/microservice-eureka-server-1.0-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build y Run

```bash
docker build -t microservice-eureka-server:1.0 .
docker run -p 8761:8761 microservice-eureka-server:1.0
```

### Docker Compose

```yaml
version: '3.8'
services:
  eureka-server:
    build: .
    ports:
      - "8761:8761"
    environment:
      - JAVA_OPTS=-Xmx512m
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8761"]
      interval: 30s
      timeout: 10s
      retries: 3
```

---

## 🧪 Testing

### Verificar que Eureka está Corriendo

```bash
curl http://localhost:8761/actuator/health
```

**Respuesta esperada:**
```json
{
  "status": "UP"
}
```

### Verificar Servicios Registrados

```bash
curl http://localhost:8761/eureka/apps | grep -o '<app>.*</app>'
```

---

## 🔐 Seguridad (Opcional)

### Activar Basic Auth

**Dependencia:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**Configuración:**
```yaml
spring:
  security:
    user:
      name: admin
      password: eureka123
```

**Configuración en Clientes:**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://admin:eureka123@localhost:8761/eureka/
```

---

## 📈 Monitoreo y Métricas

### Actuator Endpoints

**Activar Actuator:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Configuración:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
```

**Endpoints disponibles:**
- `/actuator/health` - Estado de salud
- `/actuator/info` - Información del servidor
- `/actuator/metrics` - Métricas
- `/actuator/prometheus` - Formato Prometheus

---

## 🔧 Troubleshooting

### Problema: Servicios no aparecen en Eureka

**Soluciones:**
1. Verificar que el servicio esté ejecutándose
2. Verificar que `eureka.client.register-with-eureka=true`
3. Verificar URL correcta en `defaultZone`
4. Esperar 30-60 segundos (delayed registration)
5. Revisar logs del servicio cliente

### Problema: "EMERGENCY!" en Dashboard

**Causa:** Self-preservation mode activado

**Soluciones:**
1. Verificar conectividad de red
2. En desarrollo, desactivar self-preservation
3. Verificar que los servicios estén enviando heartbeats

### Problema: Puerto 8761 ya en uso

```bash
lsof -i :8761
kill -9 <PID>
```

### Problema: Dashboard no carga

1. Verificar que Eureka esté corriendo
2. Verificar firewall
3. Verificar logs de Eureka

---

## 📊 Alta Disponibilidad (HA)

### Configuración de Múltiples Eureka Servers

**Eureka Server 1 (8761):**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8762/eureka/
  instance:
    hostname: eureka-server-1
```

**Eureka Server 2 (8762):**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    hostname: eureka-server-2
```

**Clientes:**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/
```

---

## 📚 Recursos Adicionales

- [Spring Cloud Netflix Documentation](https://spring.io/projects/spring-cloud-netflix)
- [Netflix Eureka Wiki](https://github.com/Netflix/eureka/wiki)
- [Microservices Patterns](https://microservices.io/patterns/service-registry.html)

---

## 👥 Equipo de Desarrollo

**Mantenedor:** CoopCredit Development Team  
**Versión:** 1.0-SNAPSHOT  
**Última actualización:** Diciembre 2025

---

## 📝 Notas Importantes

1. **Desarrollo vs Producción:**
   - En desarrollo: Self-preservation OFF, wait-time 0
   - En producción: Self-preservation ON, configurar thresholds

2. **Escalabilidad:**
   - Eureka soporta múltiples instancias (peer-to-peer)
   - Recomendado: Mínimo 2 instancias en producción

3. **Heartbeats:**
   - Default: cada 30 segundos
   - Timeout: 90 segundos sin heartbeat = servicio DOWN

4. **Registry Cache:**
   - Clientes cachean el registry localmente
   - Cache se actualiza cada 30 segundos
