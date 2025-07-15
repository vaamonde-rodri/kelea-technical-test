plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.rodrigovaamonde"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Dependencias de todos los módulos
    implementation(project(":application"))
    implementation(project(":driving:api-rest"))
    implementation(project(":driven:adapter-database"))

    // Spring Boot Starter para configuración
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")

    // Development tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // Monitoreo y métricas
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    // Librería principal de Resilience4j para Spring Boot
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")

    // AOP es necesario para que las anotaciones de Resilience4j funcionen
    implementation("org.springframework.boot:spring-boot-starter-aop")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
