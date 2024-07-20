package com.icare.dao.repository;

import com.icare.dao.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {
    boolean existsByName(String name);
    Optional<CityEntity> findByName(String name);
}
