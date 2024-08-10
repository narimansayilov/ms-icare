package com.icare.model.dto.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCriteriaRequest {
    private String title;
    private String description;
    private Double minPrice;
    private Double maxPrice;
    private Integer cityId;
    private Integer categoryId;
    private Boolean deliveryAvailable;
}
