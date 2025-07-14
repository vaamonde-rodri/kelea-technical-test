package dev.rodrigovaamonde.keleatechnicaltest.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Price(
        Integer brandId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer priceList,
        Long productId,
        Integer priority,
        BigDecimal price
) {
}
