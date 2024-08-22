package com.icare.repository;

import com.icare.entity.RentalEntity;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Registered
public interface RentalRepository extends JpaRepository<RentalEntity, Long> {
    List<RentalEntity> findByOrderId(Long orderId);
}
