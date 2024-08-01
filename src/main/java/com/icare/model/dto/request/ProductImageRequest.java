package com.icare.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductImageRequest {
    private Long productId;
    private String imageUrl;
    private boolean main;
}
