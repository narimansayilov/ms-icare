package com.icare.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private Long rentalId;
    private String comment;
    private Integer rating;
}
