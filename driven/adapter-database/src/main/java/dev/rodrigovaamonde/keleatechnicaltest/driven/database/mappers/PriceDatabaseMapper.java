package dev.rodrigovaamonde.keleatechnicaltest.driven.database.mappers;

import dev.rodrigovaamonde.keleatechnicaltest.driven.database.models.PriceMO;
import dev.rodrigovaamonde.keleatechnicaltest.application.models.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceDatabaseMapper {

    public Price toDomain(PriceMO priceMO) {
        return new Price(
                priceMO.getBrandId(),
                priceMO.getStartDate(),
                priceMO.getEndDate(),
                priceMO.getPriceList(),
                priceMO.getProductId(),
                priceMO.getPriority(),
                priceMO.getPrice()
        );
    }
}
