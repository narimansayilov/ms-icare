package com.icare.repository;

import com.icare.entity.FavoriteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface FavoriteRepository  extends JpaRepository<FavoriteEntity, Long>, PagingAndSortingRepository<FavoriteEntity, Long> {
    Page<FavoriteEntity> findByUserId(Long userId, Pageable pageable);
    Boolean existsByUserIdAndProductId(Long userId, Long productId);
}
