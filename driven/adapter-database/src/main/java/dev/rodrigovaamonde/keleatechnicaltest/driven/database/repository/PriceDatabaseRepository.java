package dev.rodrigovaamonde.keleatechnicaltest.driven.database.repository;

import dev.rodrigovaamonde.keleatechnicaltest.driven.database.models.PriceMO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceDatabaseRepository extends JpaRepository<PriceMO, Long> {

    @Query("SELECT p FROM PriceMO p WHERE p.brandId = :brandId AND p.productId = :productId AND :applicationDate BETWEEN p.startDate AND p.endDate")
    List<PriceMO> findApplicablePrices(
            @Param("brandId") Integer brandId,
            @Param("productId") Long productId,
            @Param("applicationDate") LocalDateTime applicationDate
    );
}
