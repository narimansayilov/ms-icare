package com.icare.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductRequest {
    private String title;
    private String description;
    private Double pricePerDay;
    private String address;
    private boolean deliveryAvailable;
    private Double deliveryPricePerKm;
    private Long userId;
    private Long categoryId;
    private Long cityId;
}
