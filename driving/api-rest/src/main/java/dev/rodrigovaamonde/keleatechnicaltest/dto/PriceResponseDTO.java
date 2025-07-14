package dev.rodrigovaamonde.keleatechnicaltest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponseDTO(
        Long productId,
        Integer brandId,
        Integer priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price
) {
}
