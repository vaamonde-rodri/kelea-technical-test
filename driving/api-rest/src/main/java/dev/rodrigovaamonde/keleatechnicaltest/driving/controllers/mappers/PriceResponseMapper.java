package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.mappers;

import dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.dto.PriceResponseDTO;
import dev.rodrigovaamonde.keleatechnicaltest.models.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceResponseMapper {
    public PriceResponseDTO toResponse(Price price) {
        return new PriceResponseDTO(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.price()
        );
    }
}
