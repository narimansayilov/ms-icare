package com.icare.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageResponse {
    private Long id;
    private boolean main;
    private String url;
}
