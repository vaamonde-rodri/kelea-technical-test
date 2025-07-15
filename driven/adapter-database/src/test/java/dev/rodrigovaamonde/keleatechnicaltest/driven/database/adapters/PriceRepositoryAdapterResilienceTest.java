package dev.rodrigovaamonde.keleatechnicaltest.driven.database.adapters;

import dev.rodrigovaamonde.keleatechnicaltest.application.models.Price;
import dev.rodrigovaamonde.keleatechnicaltest.driven.database.mappers.PriceDatabaseMapper;
import dev.rodrigovaamonde.keleatechnicaltest.driven.database.models.PriceMO;
import dev.rodrigovaamonde.keleatechnicaltest.driven.database.repository.PriceDatabaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceRepositoryAdapterResilienceTest {

    @Mock
    private PriceDatabaseRepository priceDatabaseRepository;

    @Mock
    private PriceDatabaseMapper priceDatabaseMapper;

    @InjectMocks
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @Test
    void testFindApplicablePrices_successfulMapping() {
        LocalDateTime applicationDate = LocalDateTime.now();

        // Create test entity
        PriceMO entity = new PriceMO();
        entity.setBrandId(1);
        entity.setProductId(1L);
        entity.setPrice(BigDecimal.valueOf(35.50));
        entity.setPriceList(1);
        entity.setPriority(0);
        entity.setStartDate(applicationDate.minusDays(1));
        entity.setEndDate(applicationDate.plusDays(1));

        Price domainPrice = new Price(
                1,
                applicationDate.minusDays(1),
                applicationDate.plusDays(1),
                1,
                1L,
                0,
                BigDecimal.valueOf(35.50)
        );

        when(priceDatabaseRepository.findApplicablePrices(1, 1L, applicationDate))
                .thenReturn(List.of(entity));
        when(priceDatabaseMapper.toDomain(entity)).thenReturn(domainPrice);

        List<Price> prices = priceRepositoryAdapter.findApplicablePrices(1, 1L, applicationDate);

        assertThat(prices).containsExactly(domainPrice);
    }

    @Test
    void testFallbackFindApplicablePrices_returnsEmptyList() {
        LocalDateTime applicationDate = LocalDateTime.now();
        Throwable cause = new IOException("Simulated failure");

        List<Price> prices = priceRepositoryAdapter
                .fallbackFindApplicablePrices(1, 1L, applicationDate, cause);

        assertThat(prices).isEmpty();
    }
}