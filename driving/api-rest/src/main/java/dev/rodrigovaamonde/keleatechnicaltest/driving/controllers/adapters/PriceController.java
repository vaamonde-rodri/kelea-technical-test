package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.adapters;

import dev.rodrigovaamonde.keleatechnicaltest.application.models.Price;
import dev.rodrigovaamonde.keleatechnicaltest.application.ports.driving.FindPriceUseCase;
import dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.dto.PriceResponseDTO;
import dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.mappers.PriceResponseMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Validated
public class PriceController {

    private final FindPriceUseCase findPriceUseCase;
    private final PriceResponseMapper priceResponseMapper;

    @GetMapping("/prices/query")
    public ResponseEntity<PriceResponseDTO> findApplicablePrice(
            @RequestParam
            @NotNull(message = "Application date is required")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate,

            @RequestParam
            @NotNull(message = "Product ID is required")
            @Positive(message = "Product ID must be a positive number")
            Long productId,

            @RequestParam
            @NotNull(message = "Brand ID is required")
            @Positive(message = "Brand ID must be a positive number")
            Integer brandId) {

        Price price = findPriceUseCase.findApplicablePrice(brandId, productId, applicationDate);
        return ResponseEntity.ok(priceResponseMapper.toResponse(price));
    }
}
