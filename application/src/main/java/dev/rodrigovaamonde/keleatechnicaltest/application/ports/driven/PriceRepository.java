package dev.rodrigovaamonde.keleatechnicaltest.application.ports.driven;

import dev.rodrigovaamonde.keleatechnicaltest.models.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

    List<Price> findApplicablePrices(
            Integer brandId,
            Long productId,
            LocalDateTime applicationDate
    );
}
