package dev.rodrigovaamonde.keleatechnicaltest.driven.database.adapters;

import dev.rodrigovaamonde.keleatechnicaltest.application.ports.driven.PriceRepository;
import dev.rodrigovaamonde.keleatechnicaltest.driven.database.mappers.PriceDatabaseMapper;
import dev.rodrigovaamonde.keleatechnicaltest.driven.database.repository.PriceDatabaseRepository;
import dev.rodrigovaamonde.keleatechnicaltest.application.models.Price;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PriceRepositoryAdapter implements PriceRepository {

    private final PriceDatabaseRepository priceDatabaseRepository;
    private final PriceDatabaseMapper priceDatabaseMapper;


    @Override
    @CircuitBreaker(name = "database", fallbackMethod = "fallbackFindApplicablePrices")
    public List<Price> findApplicablePrices(Integer brandId, Long productId, LocalDateTime applicationDate) {
        log.info("Calling the database to retrieve prices...");
        return priceDatabaseRepository.findApplicablePrices(brandId, productId, applicationDate)
                .stream()
                .map(priceDatabaseMapper::toDomain)
                .toList();
    }

    public List<Price> fallbackFindApplicablePrices(Integer brandId, Long productId, LocalDateTime applicationDate, Throwable cause) {
        log.warn("Fallback executed for findApplicablePrices. The circuit is open. Cause: {}", cause.toString());
        return Collections.emptyList();
    }
}
