# Prueba Técnica - API de Consulta de Precios

## Descripción del Problema

(Aquí puedes pegar la descripción del problema que te dieron en el enunciado).

---

## Decisiones de Diseño y Arquitectura

En esta sección se explican las decisiones clave tomadas durante el desarrollo.

### Arquitectura Hexagonal y Multi-módulo

La solución se ha implementado siguiendo los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)** para separar claramente la lógica de negocio de los detalles de infraestructura. Para reforzar este aislamiento, el proyecto está estructurado en un sistema multi-módulo de Gradle:

* **`application`**: Contiene el núcleo de la aplicación (modelos de dominio, puertos y servicios/casos de uso), sin ninguna dependencia de frameworks externos.
* **`driving/api-rest`**: Módulo adaptador de entrada (*Driving Adapter*). Contiene el controlador REST, DTOs y todo lo relacionado con el framework Spring Web.
* **`driven/adapter-database`**: Módulo adaptador de salida (*Driven Adapter*). Contiene la implementación del repositorio usando Spring Data JPA, las entidades y la conexión con la base de datos.
* **`boot`**: Módulo de arranque. Su única responsabilidad es arrancar el contexto de Spring y conectar todos los módulos.

Este enfoque garantiza una alta cohesión, bajo acoplamiento y una excelente capacidad para realizar tests.

### Flujo de Desarrollo (TDD)

El desarrollo se ha guiado por la metodología **Test-Driven Development (TDD)**, comenzando por un test de integración que validaba el comportamiento esperado de la API. Este test ha "conducido" la creación de cada capa de la aplicación, desde el controlador hasta la base de datos, asegurando que cada pieza de código se creaba con un propósito claro y probado.

### Sistema de Caché

Para mejorar el rendimiento de la API, se ha implementado un sistema de caché utilizando **Spring Cache**. La configuración incluye:

* **`@EnableCaching`**: Habilitado a nivel de aplicación para activar el soporte de caché
* **`@Cacheable("prices")`**: Aplicado al método `findApplicablePrice` en el servicio `PriceFinderService`
* **Caché en memoria**: Utiliza el proveedor de caché por defecto de Spring (ConcurrentMapCacheManager)

Esta implementación permite cachear los resultados de consultas de precios basándose en los parámetros de entrada (brandId, productId, applicationDate), reduciendo significativamente la carga en la base de datos para consultas repetidas y mejorando los tiempos de respuesta de la API.

### Documentación de API con OpenAPI

El proyecto incluye documentación automática de la API utilizando **SpringDoc OpenAPI 3**, que proporciona:

* **Especificación OpenAPI 3.0**: Generación automática de la documentación de la API basada en el código
* **Swagger UI**: Interfaz web interactiva para explorar y probar los endpoints de la API
* **Acceso directo**: La documentación está disponible en `/swagger-ui.html` cuando la aplicación está ejecutándose

Esta implementación permite a los desarrolladores y usuarios de la API tener acceso inmediato a una documentación actualizada y interactiva, facilitando la integración y las pruebas.

### Observabilidad y Monitoreo

El proyecto incluye un sistema completo de observabilidad implementado con **Spring Boot Actuator**, **Micrometer** y **Prometheus**:

* **Spring Boot Actuator**: Proporciona endpoints de monitoreo y gestión listos para producción
* **Micrometer + Prometheus**: Permite la recolección y exportación de métricas en formato Prometheus
* **Endpoints expuestos**: 
  - `/actuator/health`: Estado de salud de la aplicación con detalles completos
  - `/actuator/info`: Información general de la aplicación
  - `/actuator/prometheus`: Métricas en formato Prometheus para scraping

**Métricas disponibles:**
- Métricas de rendimiento de la aplicación (JVM, memoria, threads)
- Métricas de HTTP (requests, response times, status codes)
- Métricas personalizadas de caché y base de datos
- Métricas de Spring Boot (datasource, web requests, etc.)

Esta configuración permite integrar fácilmente la aplicación con sistemas de monitoreo como Grafana y alertas basadas en Prometheus.

### Tracing Distribuido

El proyecto incluye un sistema completo de **tracing distribuido** implementado con **OpenTelemetry**, **Micrometer Tracing** y **Brave**:

* **OpenTelemetry**: Estándar de observabilidad para recolección de traces, métricas y logs
* **Micrometer Tracing Bridge**: Integración nativa con Spring Boot para tracing automático
* **Brave**: Implementación de tracing distribuido que proporciona el backend
* **Zipkin Reporter**: Exportación de traces a sistemas compatibles con Zipkin

**Características implementadas:**
- **Trace automático**: Todos los requests HTTP son automáticamente instrumentados
- **Propagación de contexto**: Los trace IDs y span IDs se propagan a través de las capas de la aplicación
- **Logging enriquecido**: Los logs incluyen automáticamente `traceId` y `spanId` para correlación
- **Servicio identificado**: La aplicación se identifica como "kelea-technical-test" en los traces
- **Exportación de traces**: Configurado para exportar a logging (ampliable a Zipkin/Jaeger)

**Formato de logging con tracing:**
```
2024-07-15 10:30:45.123  INFO [kelea-technical-test,64fc729b9ac7b3a1,8b9c1a2d3e4f5678] ...
```

Esta implementación permite rastrear completamente el flujo de una petición desde la entrada por el controlador REST hasta la consulta en base de datos, facilitando el debugging y el análisis de rendimiento en entornos distribuidos.

### Validación de Entrada

El proyecto implementa un sistema robusto de **validación de entrada** utilizando **Bean Validation (JSR-303)** y **Spring Validation** para garantizar la integridad de los datos recibidos en la API:

**Características implementadas:**

* **Validaciones declarativas**: Uso de anotaciones Bean Validation en los parámetros del controlador
* **Manejo centralizado de errores**: GlobalExceptionHandler para respuestas consistentes de error
* **Mensajes descriptivos**: Mensajes de error claros y específicos en inglés
* **Códigos HTTP apropiados**: Respuestas 400 Bad Request para errores de validación

**Validaciones aplicadas:**

* **`@NotNull`**: Todos los parámetros son obligatorios (applicationDate, productId, brandId)
* **`@Positive`**: Los IDs deben ser números positivos (productId > 0, brandId > 0)
* **`@DateTimeFormat`**: Formato ISO de fecha/hora para applicationDate

**Tipos de errores manejados:**

1. **Validation Error (400)**: Violaciones de las reglas de validación
   - IDs negativos o cero
   - Valores nulos en parámetros obligatorios

2. **Missing Required Parameter (400)**: Parámetros faltantes en la request
   - Ausencia de parámetros obligatorios

3. **Invalid Parameter Type (400)**: Tipos de datos incorrectos
   - Texto en lugar de números para IDs
   - Formato de fecha inválido

**Ejemplo de respuesta de error:**
```json
{
  "timestamp": "2024-07-15T10:30:45.123",
  "status": 400,
  "error": "Validation Error",
  "message": "Product ID must be a positive number",
  "path": "/prices/query"
}
```

**Tests de validación:**
- ✅ Tests comprehensivos para todos los casos de error
- ✅ Verificación de códigos de estado HTTP correctos
- ✅ Validación de estructura de respuestas de error
- ✅ Cobertura de casos límite (IDs negativos, cero, tipos incorrectos)

Esta implementación asegura que la API rechace elegantemente las entradas inválidas antes de procesar la lógica de negocio, mejorando la robustez y la experiencia del usuario.

### Resilience y Circuit Breaker

El proyecto incorpora mecanismos de resiliencia usando Resilience4j y patrones de Circuit Breaker para garantizar estabilidad y tolerancia a fallos en entornos distribuidos:

* **Circuit Breaker**: Protege llamadas a servicios externos, abre el circuito tras un umbral de errores y aplica un período de espera antes de reintentar.
* **Retry**: Reintentos configurables en caso de errores transitorios.
* **Bulkhead**: Aislamiento de recursos para limitar la concurrencia y evitar la saturación del sistema.
* **TimeLimiter**: Define tiempos máximos de espera para llamadas externas, evitando bloqueos prolongados.
* **Fallback Methods**: Métodos de reserva que proporcionan respuestas por defecto cuando la llamada primaria falla.

**Configuración**:  
Se puede ajustar en `application.yml` bajo la sección `resilience4j`, definiendo propiedades para circuitbreaker, retry, bulkhead y timelimiter.

---

## Stack Tecnológico

* **Lenguaje**: Java 21
* **Framework**: Spring Boot 3.x
* **Build Tool**: Gradle con Kotlin DSL
* **Base de Datos**: H2 in-memory
* **Testing**: JUnit 5, MockMvc
* **Documentación**: SpringDoc OpenAPI 3
* **Observabilidad**: Spring Boot Actuator + Micrometer + Prometheus
* **Tracing Distribuido**: OpenTelemetry + Micrometer Tracing + Brave + Zipkin
* **Otros**: Lombok

---

## Cómo Ejecutar la Aplicación

1.  Clonar el repositorio.
2.  Desde la raíz del proyecto, ejecutar el siguiente comando:

    ```bash
    ./gradlew :boot:bootRun
    ```
3.  La API estará disponible en `http://localhost:8080`.

## Cómo Ejecutar los Tests

Para ejecutar la suite completa de tests de integración, usa el comando:

```bash
./gradlew test
```
---
## Uso de la API

El endpoint principal para la consulta de precios es:

`GET /prices/query`

**Parámetros:**

* `applicationDate` (LocalDateTime): Fecha y hora de la consulta. Formato: `YYYY-MM-DDTHH:MM:SS`
* `productId` (Long): Identificador del producto.
* `brandId` (Integer): Identificador de la cadena.

**Ejemplos con `curl`:**

```bash
# Test 1: Petición a las 10:00 del día 14
curl --location 'http://localhost:8080/prices/query?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1'

# Test 2: Petición a las 16:00 del día 14
curl --location 'http://localhost:8080/prices/query?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1'

# Test 3: Petición a las 21:00 del día 14
curl --location 'http://localhost:8080/prices/query?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1'

# Test 4: Petición a las 10:00 del día 15
curl --location 'http://localhost:8080/prices/query?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1'

# Test 5: Petición a las 21:00 del día 16
curl --location 'http://localhost:8080/prices/query?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1'
```

## Endpoints de Monitoreo

Una vez que la aplicación esté ejecutándose, puedes acceder a los siguientes endpoints de monitoreo:

* **Documentación de la API**: `http://localhost:8080/swagger-ui.html`
* **Estado de salud**: `http://localhost:8080/actuator/health`
* **Información de la aplicación**: `http://localhost:8080/actuator/info`
* **Métricas de Prometheus**: `http://localhost:8080/actuator/prometheus`

---
## Próximos Pasos y Mejoras Propuestas

* ✅ **Observabilidad**: ~~Añadir Micrometer y Prometheus para la exportación de métricas~~ - **IMPLEMENTADO**
* ✅ **Tracing Distribuido**: ~~Añadir OpenTelemetry para trazabilidad completa de requests~~ - **IMPLEMENTADO**
* **Validación de Entrada**: Añadir validaciones en los parámetros del controlador para manejar entradas incorrectas (ej. IDs negativos)
* **Dashboards**: Crear dashboards de Grafana para visualización de métricas
* **Alertas**: Configurar alertas basadas en métricas de Prometheus
* **Exportación de Traces**: Configurar exportación a Zipkin o Jaeger para visualización de traces
* **CI/CD**: Configurar una pipeline de integración y despliegue continuo
