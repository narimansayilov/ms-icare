package com.icare.service.specification;

import com.icare.entity.ProductEntity;
import com.icare.dto.criteria.ProductCriteriaRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification implements Specification<ProductEntity>{

    public static Specification<ProductEntity> getProductByCriteria(ProductCriteriaRequest criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getTitle() != null && !criteria.getTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"));
            }

            if (criteria.getDescription() != null && !criteria.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + criteria.getDescription().toLowerCase() + "%"));
            }

            if (criteria.getMinPrice() != null && criteria.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.between(root.get("pricePerDay"), criteria.getMinPrice(), criteria.getMaxPrice()));
            } else if (criteria.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("pricePerDay"), criteria.getMinPrice()));
            } else if (criteria.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pricePerDay"), criteria.getMaxPrice()));
            }

            if (criteria.getCityId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("city").get("id"), criteria.getCityId()));
            }

            if (criteria.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), criteria.getCategoryId()));
            }

            if (criteria.getDeliveryAvailable() != null) {
                predicates.add(criteriaBuilder.equal(root.get("deliveryAvailable"), criteria.getDeliveryAvailable()));
            }

            predicates.add(criteriaBuilder.isTrue(root.get("status")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ProductEntity> getProductByUserAndStatus(Long userId, Boolean status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }

            if(status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Predicate toPredicate(Root<ProductEntity> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
