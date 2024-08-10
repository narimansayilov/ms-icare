package com.icare.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductRequest {
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 100 characters")
    private String title;

    @Size(max = 2000, message = "Description must be up to 2000 characters")
    private String description;

    @NotNull(message = "Price per day cannot be null")
    @DecimalMin(value = "0.1", message = "Price per day must be greater than zero")
    private Double pricePerDay;

    @NotBlank(message = "Address cannot be blank")
    @Size(min = 3, max = 511, message = "Address must be between 3 and 511 characters")
    private String address;

    @NotNull(message = "Delivery availability cannot be null")
    private Boolean deliveryAvailable;
    private Double deliveryPricePerKm;

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be a positive number")
    private Long categoryId;

    @NotNull(message = "City ID cannot be null")
    @Positive(message = "Cİty ID must be a positive number")
    private Long cityId;
}