# 📊 Risk Central Mock Service

## 📋 Descripción General

Servicio simulado (mock) de evaluación de riesgo crediticio para el sistema CoopCredit. Proporciona calificaciones de riesgo determinísticas basadas en el documento del solicitante, sin necesidad de conexión a sistemas externos reales. Ideal para desarrollo, testing y demos.

---

## 🎯 Propósito

- Simular evaluación de riesgo crediticio
- Generar scores crediticios determinísticos (300-950)
- Clasificar riesgo en tres niveles: ALTO, MEDIO, BAJO
- Proporcionar respuestas inmediatas sin latencia externa
- Facilitar pruebas sin dependencias de servicios reales
- Servicio stateless (sin base de datos)

---

## 🏗️ Arquitectura

```
risk-central-mock-service/
├── RiskCentralMockApplication.java  # Clase principal
└── RiskEvaluationController.java    # REST Controller
```

**Patrón:** Servicio simple sin capas (single-tier)  
**Persistencia:** Ninguna (stateless)  
**Base de datos:** No requerida

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje de programación |
| Spring Boot Web | 4.0.0 | Framework REST |
| Lombok | 1.18.30 | Reducción de boilerplate |
| Maven | 3.8+ | Build tool |

**Nota:** Este es el microservicio más ligero del ecosistema.

---

## 📡 API Endpoints

### Evaluar Riesgo Crediticio

```http
POST /risk-evaluation
Content-Type: application/json

{
  "documento": "12345678",
  "monto": 10000.00,
  "plazo": 12
}
```

**Respuesta (200 OK):**
```json
{
  "documento": "12345678",
  "score": 750,
  "nivelRiesgo": "BAJO",
  "detalle": "Excelente historial crediticio."
}
```

### Campos de Request

| Campo | Tipo | Descripción | Requerido |
|-------|------|-------------|-----------|
| `documento` | String | Documento de identidad | ✅ Sí |
| `monto` | BigDecimal | Monto del crédito solicitado | ✅ Sí |
| `plazo` | Integer | Plazo en meses | ✅ Sí |

**Nota:** Aunque `monto` y `plazo` son enviados, actualmente NO afectan el score (solo `documento`).

### Campos de Response

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `documento` | String | Documento evaluado |
| `score` | Integer | Puntaje crediticio (300-950) |
| `nivelRiesgo` | String | Clasificación: ALTO, MEDIO, BAJO |
| `detalle` | String | Descripción del resultado |

---

## 🧮 Algoritmo de Evaluación

### Generación de Score

```java
// 1. Convertir documento a seed (hash mod 1000)
long seed = Math.abs(documento.hashCode()) % 1000;

// 2. Generar score (300-950) basado en seed
// Range: 950 - 300 = 650
int score = 300 + (int) (seed * 650 / 1000);
```

### Clasificación de Riesgo

| Score | Nivel de Riesgo | Detalle |
|-------|-----------------|---------|
| 300-500 | **ALTO** | "Historial crediticio deficiente o insuficiente." |
| 501-700 | **MEDIO** | "Historial crediticio moderado." |
| 701-950 | **BAJO** | "Excelente historial crediticio." |

### Características del Algoritmo

✅ **Determinístico:** Mismo documento = mismo score siempre  
✅ **Consistente:** Resultados reproducibles para testing  
✅ **Rápido:** Sin I/O, solo cálculos matemáticos  
✅ **Sin estado:** No requiere persistencia  

---

## ⚙️ Configuración

### application.yml

```yaml
server:
  port: 8083

spring:
  application:
    name: risk-central-mock-service
```

**Nota:** Puerto cambiado a 8083 para evitar conflictos con otras aplicaciones.

---

## 🚀 Instalación y Ejecución

### Prerequisitos
- Java 17+
- Maven 3.8+

**No requiere:**
- ❌ Base de datos
- ❌ Eureka Server
- ❌ Otros servicios

### 1. Compilar
```bash
cd risk-central-mock-service
mvn clean install -DskipTests
```

### 2. Ejecutar
```bash
mvn spring-boot:run
```

O como JAR:
```bash
mvn package
java -jar target/risk-central-mock-service-1.0-SNAPSHOT.jar
```

### 3. Verificar
```bash
curl -X POST http://localhost:8083/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{
    "documento": "12345678",
    "monto": 10000,
    "plazo": 12
  }'
```

---

## 🧪 Testing

### Ejemplos de Evaluación

**Caso 1: Documento con buen score**
```bash
curl -X POST http://localhost:8083/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{
    "documento": "87654321",
    "monto": 15000,
    "plazo": 24
  }'
```

**Caso 2: Documento con score medio**
```bash
curl -X POST http://localhost:8083/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{
    "documento": "45678901",
    "monto": 8000,
    "plazo": 18
  }'
```

**Caso 3: Documento con score bajo (alto riesgo)**
```bash
curl -X POST http://localhost:8083/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{
    "documento": "11111111",
    "monto": 20000,
    "plazo": 36
  }'
```

### Tabla de Documentos de Prueba

| Documento | Score Aproximado | Nivel de Riesgo |
|-----------|------------------|-----------------|
| 12345678 | ~750 | BAJO |
| 87654321 | ~680 | MEDIO |
| 11111111 | ~450 | ALTO |
| 99999999 | ~820 | BAJO |
| 55555555 | ~590 | MEDIO |

**Nota:** Los scores exactos dependen del algoritmo hash de Java.

---

## 🔗 Integración con Credit Application Service

### Uso desde Microservicio de Crédito

```java
@Service
public class RiskEvaluationService {
    
    private final WebClient webClient;
    
    public RiskEvaluationResponse evaluateRisk(String documento, BigDecimal monto, Integer plazo) {
        return webClient.post()
            .uri("http://localhost:8083/risk-evaluation")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new RiskEvaluationRequest(documento, monto, plazo))
            .retrieve()
            .bodyToMono(RiskEvaluationResponse.class)
            .block();
    }
}
```

### Con Eureka Discovery (Futuro)

```java
@LoadBalanced
@Bean
public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
}

// Usar nombre del servicio en vez de URL hardcoded
.uri("http://risk-central-mock-service/risk-evaluation")
```

---

## 📊 Modelo de Datos

### RiskEvaluationRequest (Input)

```java
record RiskEvaluationRequest(
    String documento,
    BigDecimal monto,
    Integer plazo
) {}
```

### RiskEvaluationResponse (Output)

```java
record RiskEvaluationResponse(
    String documento,
    Integer score,
    String nivelRiesgo,
    String detalle
) {}
```

---

## 🐳 Docker

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/risk-central-mock-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build y Run

```bash
docker build -t risk-central-mock-service:1.0 .
docker run -p 8083:8083 risk-central-mock-service:1.0
```

### Docker Compose

```yaml
version: '3.8'
services:
  risk-service:
    build: .
    ports:
      - "8083:8083"
    environment:
      - JAVA_OPTS=-Xmx256m
```

---

## 🔄 Evolución a Servicio Real

### Mejoras Futuras

**Fase 1: Enriquecimiento del Algoritmo**
- Considerar `monto` y `plazo` en el cálculo
- Agregar pesos a diferentes factores
- Implementar múltiples algoritmos de scoring

**Fase 2: Persistencia**
- Guardar historial de evaluaciones
- Base de datos para tracking
- Analytics de scoring

**Fase 3: Integración Externa**
- Conectar con bureaus de crédito reales
- APIs de validación de identidad
- Sistemas de scoring comerciales

**Fase 4: Machine Learning**
- Modelo predictivo basado en datos históricos
- Scoring dinámico
- Detección de fraude

### Reemplazar con Servicio Real

1. Mantener misma interfaz REST
2. Cambiar implementación interna
3. Actualizar configuración en servicios clientes
4. Sin cambios en contratos de API

---

## ⚠️ Limitaciones

1. **Algoritmo Simplificado:** No refleja evaluación de riesgo real
2. **Sin Persistencia:** No guarda histórico de evaluaciones
3. **Determinístico:** Siempre mismo resultado para mismo documento
4. **Sin Validaciones:** No valida formato de documento
5. **Monto/Plazo Ignorados:** Solo usa documento para calcular score

**⚠️ NO USAR EN PRODUCCIÓN REAL** - Solo para desarrollo y testing.

---

## 🔧 Troubleshooting

### Error: Puerto 8083 ya en uso

```bash
lsof -i :8083
kill -9 <PID>
```

### Error: Conexión rechazada desde otro servicio

1. Verificar que el servicio esté corriendo:
```bash
curl http://localhost:8083/risk-evaluation \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"documento":"123","monto":1000,"plazo":12}'
```

2. Verificar firewall/red

### Error de compilación

```bash
mvn clean install -U -DskipTests
```

---

## 🔐 Seguridad

**Estado Actual:**
- ✅ Sin autenticación requerida
- ✅ Endpoint público
- ✅ Sin datos sensibles almacenados

**Para Producción (servicio real):**
- Implementar autenticación API Key
- Rate limiting
- Validación de entrada
- Logging de auditoría
- Encriptación de datos sensibles

---

## 📈 Métricas y Monitoreo

### Agregar Actuator (Opcional)

**Dependencia:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Endpoints disponibles:**
- `/actuator/health` - Estado de salud
- `/actuator/metrics` - Métricas

---

## 📚 Recursos Adicionales

- [Spring Boot REST Documentation](https://spring.io/guides/tutorials/rest/)
- [Credit Scoring Basics](https://www.investopedia.com/terms/c/credit_scoring.asp)
- [Microservices Testing Strategies](https://martinfowler.com/articles/microservice-testing/)

---

## 👥 Equipo de Desarrollo

**Mantenedor:** CoopCredit Development Team  
**Versión:** 1.0-SNAPSHOT  
**Última actualización:** Diciembre 2025

---

## 📝 Notas de Desarrollo

### Uso Recomendado

✅ **Bueno para:**
- Desarrollo local
- Testing automatizado
- Demos
- Pruebas de integración
- Entornos de staging

❌ **No usar para:**
- Producción
- Evaluaciones de crédito reales
- Toma de decisiones financieras

### Testing Determinístico

Aprovecha que el servicio es determinístico para tests:

```java
@Test
public void testSameDocumentReturnsSameScore() {
    Response resp1 = evaluateRisk("12345678", 10000, 12);
    Response resp2 = evaluateRisk("12345678", 20000, 24);
    
    assertEquals(resp1.score(), resp2.score());
    // Monto y plazo diferentes, pero score igual
}
```

---

## 🎯 Próximos Pasos

1. Registrar en Eureka Server
2. Agregar validaciones de entrada
3. Implementar logging estructurado
4. Agregar métricas de uso
5. Documentar OpenAPI/Swagger
6. Implementar circuit breaker para resiliencia
