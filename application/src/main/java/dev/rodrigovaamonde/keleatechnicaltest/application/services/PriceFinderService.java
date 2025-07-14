package dev.rodrigovaamonde.keleatechnicaltest.application.services;

import dev.rodrigovaamonde.keleatechnicaltest.application.exceptions.PriceNotFoundException;
import dev.rodrigovaamonde.keleatechnicaltest.application.ports.driven.PriceRepository;
import dev.rodrigovaamonde.keleatechnicaltest.application.ports.driving.FindPriceUseCase;
import dev.rodrigovaamonde.keleatechnicaltest.application.models.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceFinderService implements FindPriceUseCase {

    private final PriceRepository priceRepository;

    @Override
    @Cacheable("prices")
    public Price findApplicablePrice(
            Integer brandId,
            Long productId,
            LocalDateTime applicationDate
    ) {
        return priceRepository.findApplicablePrices(brandId, productId, applicationDate)
                .stream()
                .max(Comparator.comparing(Price::priority))
                .orElseThrow(() -> new PriceNotFoundException("No applicable price found for the given criteria."));
    }
}
