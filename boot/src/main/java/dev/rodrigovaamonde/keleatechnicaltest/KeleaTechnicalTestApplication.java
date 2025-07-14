package dev.rodrigovaamonde.keleatechnicaltest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "dev.rodrigovaamonde")
public class KeleaTechnicalTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeleaTechnicalTestApplication.class, args);
    }
}
