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

---

## Stack Tecnológico

* **Lenguaje**: Java 21
* **Framework**: Spring Boot 3.x
* **Build Tool**: Gradle con Kotlin DSL
* **Base de Datos**: H2 in-memory
* **Testing**: JUnit 5, MockMvc
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

---
## Próximos Pasos y Mejoras Propuestas

* **Observabilidad**: Añadir Micrometer y Prometheus para la exportación de métricas, y un sistema de tracing distribuido como OpenTelemetry.
* **Caching**: Implementar un mecanismo de caché (ej. con Caffeine y Spring Cache) en el `PriceFinderService` para optimizar las peticiones recurrentes.
* **Documentación de API**: Integrar `springdoc-openapi` para generar automáticamente una especificación OpenAPI y una UI de Swagger.
* **Validación de Entrada**: Añadir validaciones en los parámetros del controlador para manejar entradas incorrectas (ej. IDs negativos).
* **CI/CD**: Configurar una pipeline de integración y despliegue continuo.