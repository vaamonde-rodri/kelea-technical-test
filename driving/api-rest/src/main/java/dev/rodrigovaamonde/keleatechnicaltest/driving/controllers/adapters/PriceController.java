package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.adapters;

import dev.rodrigovaamonde.keleatechnicaltest.application.ports.driving.FindPriceUseCase;
import dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.dto.PriceResponseDTO;
import dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.mappers.PriceResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class PriceController {

    private final FindPriceUseCase findPriceUseCase;
    private final PriceResponseMapper priceResponseMapper;

    @GetMapping("/prices/query")
    public ResponseEntity<PriceResponseDTO> findApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam Long productId,
            @RequestParam Integer brandId) {

        return findPriceUseCase.findApplicablePrice(brandId, productId, applicationDate)
                .map(priceResponseMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
