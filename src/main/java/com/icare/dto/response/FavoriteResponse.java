package com.icare.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FavoriteResponse {
    private Long id;
    private ProductResponse product;
    private LocalDateTime createdAt;
}
