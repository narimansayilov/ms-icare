package com.icare.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelResponse {
    private Long id;
    private String name;
    private Integer adLimit;
    private Double price;
}