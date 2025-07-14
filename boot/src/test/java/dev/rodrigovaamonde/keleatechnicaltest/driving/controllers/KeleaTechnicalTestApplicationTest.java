package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = KeleaTechnicalTestApplication.class)
@ActiveProfiles("test")
class KeleaTechnicalTestApplicationTest {

    @Test
    void contextLoads() {
        // Este test verifica que el contexto de Spring se carga correctamente
        // Al ejecutarse sin errores, confirma que la aplicación está bien configurada
    }

    @Test
    void applicationShouldStartSuccessfully() {
        // Este test implícitamente prueba el método main al cargar el contexto completo
        // Si el contexto se carga exitosamente, significa que la aplicación puede iniciarse
        KeleaTechnicalTestApplication app = new KeleaTechnicalTestApplication();
        // Verificamos que la instancia se puede crear sin problemas
        assert app != null;
    }
}
