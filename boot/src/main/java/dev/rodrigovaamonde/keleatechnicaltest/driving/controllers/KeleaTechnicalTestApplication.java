package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = "dev.rodrigovaamonde")
@EnableJpaRepositories(basePackages = "dev.rodrigovaamonde.keleatechnicaltest.driven.database.repository")
@EntityScan(basePackages = "dev.rodrigovaamonde.keleatechnicaltest.driven.database.models")
public class KeleaTechnicalTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeleaTechnicalTestApplication.class, args);
    }
}
