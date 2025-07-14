package dev.rodrigovaamonde.keleatechnicaltest.driven.database.adapters;

import dev.rodrigovaamonde.keleatechnicaltest.application.ports.driven.PriceRepository;
import dev.rodrigovaamonde.keleatechnicaltest.driven.database.mappers.PriceDatabaseMapper;
import dev.rodrigovaamonde.keleatechnicaltest.driven.database.repository.PriceDatabaseRepository;
import dev.rodrigovaamonde.keleatechnicaltest.models.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

    private final PriceDatabaseRepository priceDatabaseRepository;
    private final PriceDatabaseMapper priceDatabaseMapper;


    @Override
    public List<Price> findApplicablePrices(Integer brandId, Long productId, LocalDateTime applicationDate) {
        return priceDatabaseRepository.findApplicablePrices(brandId, productId, applicationDate)
                .stream()
                .map(priceDatabaseMapper::toDomain)
                .toList();
    }
}
