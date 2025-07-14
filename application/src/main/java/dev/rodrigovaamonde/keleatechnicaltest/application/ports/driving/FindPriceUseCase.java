package dev.rodrigovaamonde.keleatechnicaltest.application.ports.driving;

import dev.rodrigovaamonde.keleatechnicaltest.models.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FindPriceUseCase {

    Optional<Price> findApplicablePrice(
            Integer brandId,
            Long productId,
            LocalDateTime applicationDate
    );
}
