package com.icare.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private Long productId;
    private String comment;
    private Integer rating;
    private LocalDate createdAt;
    private UserResponse user;
}
