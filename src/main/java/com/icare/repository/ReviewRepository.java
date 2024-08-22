package com.icare.repository;

import com.icare.entity.ReviewEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>, PagingAndSortingRepository<ReviewEntity, Long>{
    List<ReviewEntity> findAllByProductIdAndStatus(Long productId, Boolean status);
    Page<ReviewEntity> findAllByProductIdAndStatus(Long productId, Pageable pageable, Boolean status);
}
