package com.icare.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private Double pricePerDay;
    private String address;
    private Boolean deliveryAvailable;
    private Double deliveryPricePerKm;
    private UserResponse user;
    private Integer categoryId;
    private Integer cityId;
    private Double totalRating;
    private Integer viewCount;
    private Integer rentalCount;
    private Integer reviewCount;
    private LocalDateTime createdAt;
    private List<ProductImageResponse> images;
}