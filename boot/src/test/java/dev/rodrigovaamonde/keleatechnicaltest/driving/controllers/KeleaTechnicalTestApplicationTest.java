package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.cache.CacheManager;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KeleaTechnicalTestApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void contextLoads() {
        // Verifica que el contexto de la aplicación se cargue correctamente
        assertNotNull(applicationContext, "El contexto de la aplicación no debería ser nulo");
    }

    @Test
    void verifyBeanInitialization() {
        // Verifica que el bean principal esté inicializado
        assertNotNull(applicationContext.getBean(KeleaTechnicalTestApplication.class),
                     "El bean principal debería estar inicializado");
    }

    @Test
    void verifyCachingEnabled() {
        // Verifica que el CacheManager esté configurado correctamente debido a @EnableCaching
        assertNotNull(cacheManager, "El CacheManager debería estar configurado");
    }

    @Test
    void verifyAnnotationsPresent() {
        // Verifica que las anotaciones necesarias estén presentes en la clase
        Class<KeleaTechnicalTestApplication> appClass = KeleaTechnicalTestApplication.class;

        assertTrue(appClass.isAnnotationPresent(SpringBootApplication.class),
                  "La clase debería tener la anotación @SpringBootApplication");
        assertTrue(appClass.isAnnotationPresent(EnableCaching.class),
                  "La clase debería tener la anotación @EnableCaching");
        assertTrue(appClass.isAnnotationPresent(ComponentScan.class),
                  "La clase debería tener la anotación @ComponentScan");
        assertTrue(appClass.isAnnotationPresent(EnableJpaRepositories.class),
                  "La clase debería tener la anotación @EnableJpaRepositories");
        assertTrue(appClass.isAnnotationPresent(EntityScan.class),
                  "La clase debería tener la anotación @EntityScan");
    }

    @Test
    void verifyComponentScanConfiguration() {
        // Verifica la configuración específica de @ComponentScan
        ComponentScan componentScan = KeleaTechnicalTestApplication.class.getAnnotation(ComponentScan.class);
        String[] basePackages = componentScan.basePackages();
        assertEquals(1, basePackages.length, "Debería haber exactamente un paquete base configurado");
        assertEquals("dev.rodrigovaamonde", basePackages[0], "El paquete base debería ser 'dev.rodrigovaamonde'");
    }

    @Test
    void verifyJpaRepositoriesConfiguration() {
        // Verifica la configuración específica de @EnableJpaRepositories
        EnableJpaRepositories jpaRepos = KeleaTechnicalTestApplication.class.getAnnotation(EnableJpaRepositories.class);
        String[] basePackages = jpaRepos.basePackages();
        assertEquals(1, basePackages.length, "Debería haber exactamente un paquete base para repositorios");
        assertEquals("dev.rodrigovaamonde.keleatechnicaltest.driven.database.repository", basePackages[0],
                    "El paquete base para repositorios debería ser el correcto");
    }

    @Test
    void verifyEntityScanConfiguration() {
        // Verifica la configuración específica de @EntityScan
        EntityScan entityScan = KeleaTechnicalTestApplication.class.getAnnotation(EntityScan.class);
        String[] basePackages = entityScan.basePackages();
        assertEquals(1, basePackages.length, "Debería haber exactamente un paquete base para entidades");
        assertEquals("dev.rodrigovaamonde.keleatechnicaltest.driven.database.models", basePackages[0],
                    "El paquete base para entidades debería ser el correcto");
    }

    @Test
    void testMainMethodDoesNotThrowException() {
        // Verifica que el método main no lance excepciones con argumentos vacíos
        // Nota: Este test no inicia realmente la aplicación para evitar conflictos en el contexto de prueba
        assertDoesNotThrow(() -> {
            String[] args = {};
            // Verificamos que la clase y método existen sin ejecutar realmente la aplicación
            assertNotNull(KeleaTechnicalTestApplication.class.getDeclaredMethod("main", String[].class),
                         "El método main debería existir");
        }, "El método main no debería lanzar excepciones durante la verificación");
    }
}
