package com.icare.repository;

import com.icare.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long>, PagingAndSortingRepository<CityEntity, Long> {
}