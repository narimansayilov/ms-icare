package com.icare.repository;

import com.icare.entity.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Long>, PagingAndSortingRepository<LevelEntity, Long> {
    Optional<LevelEntity> findByName(String name);
}
