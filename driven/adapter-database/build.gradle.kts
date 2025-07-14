plugins {
    id("java")
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

    // Spring Data JPA para persistencia
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Base de datos H2 para desarrollo
    runtimeOnly("com.h2database:h2")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.assertj:assertj-core")
}

tasks.test {
    useJUnitPlatform()
}