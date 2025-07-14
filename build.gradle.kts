plugins {
    java
    jacoco
}

group = "dev.rodrigovaamonde"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")

    group = "dev.rodrigovaamonde"
    version = "0.0.1-SNAPSHOT"

    configure<JacocoPluginExtension> {
        toolVersion = "0.8.11"
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
        finalizedBy(tasks.named("jacocoTestReport"))
    }

    tasks.named<JacocoReport>("jacocoTestReport") {
        dependsOn(tasks.named("test"))
        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }
    }

    tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
        violationRules {
            rule {
                limit {
                    minimum = "0.50".toBigDecimal()
                }
            }
        }
    }
}

// Tarea para generar reporte de cobertura consolidado
tasks.register<JacocoReport>("jacocoAggregatedReport") {
    description = "Generates aggregated Jacoco coverage report for all modules"
    group = "verification"

    dependsOn(subprojects.map { it.tasks.named("test") })
    dependsOn(subprojects.map { it.tasks.named("jacocoTestReport") })

    additionalSourceDirs.setFrom(subprojects.map { it.sourceSets.main.get().allSource.srcDirs })
    sourceDirectories.setFrom(subprojects.map { it.sourceSets.main.get().allSource.srcDirs })
    classDirectories.setFrom(subprojects.map { it.sourceSets.main.get().output })
    executionData.setFrom(project.fileTree(mapOf("dir" to ".", "include" to "**/build/jacoco/test.exec")))

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.register("jacocoAggregatedCoverageVerification", JacocoCoverageVerification::class) {
    description = "Verifies aggregated code coverage metrics"
    group = "verification"

    dependsOn("jacocoAggregatedReport")

    additionalSourceDirs.setFrom(subprojects.map { it.sourceSets.main.get().allSource.srcDirs })
    sourceDirectories.setFrom(subprojects.map { it.sourceSets.main.get().allSource.srcDirs })
    classDirectories.setFrom(subprojects.map { it.sourceSets.main.get().output })
    executionData.setFrom(project.fileTree(mapOf("dir" to ".", "include" to "**/build/jacoco/test.exec")))

    violationRules {
        rule {
            limit {
                minimum = "0.70".toBigDecimal()
            }
        }
    }
}
