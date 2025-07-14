package dev.rodrigovaamonde.keleatechnicaltest;

import dev.rodrigovaamonde.keleatechnicaltest.dto.PriceResponseDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
public class PriceController {

    @GetMapping("/prices/query")
    public ResponseEntity<PriceResponseDTO> findApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam Long productId,
            @RequestParam Integer brandId) {

        // Simulación de lógica para determinar el precio aplicable
        var response = new PriceResponseDTO(
                productId,
                brandId,
                1,
                applicationDate.minusDays(1),
                applicationDate.plusDays(1),
                new BigDecimal("35.50")
        );

        return ResponseEntity.ok(response);
    }
}
