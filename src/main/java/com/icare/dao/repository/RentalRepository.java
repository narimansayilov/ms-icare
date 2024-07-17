package com.icare.dao.repository;

import com.icare.dao.entity.RentalEntity;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface RentalRepository extends JpaRepository<RentalEntity, Long> {
}
