package dev.rodrigovaamonde;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testEndpoint() {
        return "Endpoint de prueba funcionando correctamente";
    }

    @GetMapping("/variable/{value}")
    public String variableEndpoint(@PathVariable("value") String value) {
        return "Valor recibido: " + value;
    }
}
