# 🎲 Risk Central Mock Service - Evaluación de Riesgo Crediticio

Servicio mock para evaluación de riesgo crediticio con scoring determinístico.

## 🎯 Características

- Evaluación de riesgo basada en documento
- Scoring de 300 a 950 puntos
- Clasificación: ALTO, MEDIO, BAJO
- Algoritmo determinístico (mismo documento = mismo score)
- Sin base de datos (stateless)

## 📡 Endpoint

### Evaluar Riesgo Crediticio
```
POST /risk-evaluation
Content-Type: application/json

{
  "documento": "12345678",
  "monto": 100000,
  "plazo": 12
}
```

**Response:**
```json
{
  "documento": "12345678",
  "score": 521,
  "nivelRiesgo": "MEDIO",
  "detalle": "Historial crediticio moderado."
}
```

## 📊 Niveles de Riesgo

| Score | Nivel | Detalle |
|-------|-------|---------|
| 300-500 | **ALTO** | Historial crediticio deficiente o insuficiente |
| 501-700 | **MEDIO** | Historial crediticio moderado |
| 701-950 | **BAJO** | Excelente historial crediticio |

## 🧮 Algoritmo

El score se calcula de forma determinística usando el hash del documento:

```java
long seed = Math.abs(documento.hashCode()) % 1000;
int score = 300 + (int) (seed * 650 / 1000);
```

**Ejemplos:**
- Documento `"99999999"` → Score 653 (MEDIO)
- Documento `"12345678"` → Score 521 (MEDIO)
- Documento `"11111111"` → Score 461 (ALTO)

## 🚀 Ejecución

```bash
mvn spring-boot:run
```

**Puerto:** 8083

## 🔧 Configuración

`application.yml`:
```yaml
server:
  port: 8083

spring:
  application:
    name: risk-central-mock-service
```

## 📝 Notas

- Este es un servicio **MOCK** para desarrollo/testing
- En producción debe integrarse con un servicio real de credit bureau
- Los scores son **determinísticos** (útil para testing)
- No requiere base de datos
- No requiere autenticación
- Stateless (sin estado)

## 🧪 Casos de Prueba

### Riesgo BAJO
```json
{
  "documento": "88888888",
  "monto": 200000,
  "plazo": 24
}
```

### Riesgo MEDIO
```json
{
  "documento": "12345678",
  "monto": 150000,
  "plazo": 18
}
```

### Riesgo ALTO
```json
{
  "documento": "11111111",
  "monto": 50000,
  "plazo": 12
}
```

## 🏗️ Estructura

```
risk-central-mock-service/
├── RiskCentralMockApplication.java
├── RiskEvaluationController.java
└── application.yml
```

## 📦 Dependencias

- Spring Boot Web
- Lombok

## ✅ Estado

- ✅ Evaluación funcionando
- ✅ Scoring determinístico OK
- ✅ Sin dependencias externas
- ✅ Listo para integración
