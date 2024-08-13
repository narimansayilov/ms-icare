package com.icare.repository;

import com.icare.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository  extends JpaRepository<FavoriteEntity, Long> {
}
