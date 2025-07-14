plugins {
    id("java")
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

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.3")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Dependencia del módulo de aplicación
    implementation(project(":application"))

    // Dependencia del módulo boot para acceso a la configuración de Spring Boot
    testImplementation(project(":boot"))

    // Spring Web para REST APIs
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing - usar las versiones gestionadas por Spring Boot
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")

    }

    // OpenAPI para documentación de la API REST
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
