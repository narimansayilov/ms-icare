package com.icare.repository;

import com.icare.entity.RentalEntity;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Registered
public interface RentalRepository extends JpaRepository<RentalEntity, Long> {
    List<RentalEntity> findByOrderId(Long orderId);

    @Query("select r from RentalEntity r where r.product.id = :productId and r.rentalEndDate >= :endDate and r.status <> 'CANCELED'")
    List<RentalEntity> findActiveRentalsByProductIdAndStartDate(
            @Param("productId") Long productId,
            @Param("endDate") LocalDate endDate
    );
}
