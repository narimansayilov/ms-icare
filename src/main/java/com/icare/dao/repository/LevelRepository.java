package com.icare.dao.repository;

import com.icare.dao.entity.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Long> {
    Optional<LevelEntity> findByName(String name);
}
