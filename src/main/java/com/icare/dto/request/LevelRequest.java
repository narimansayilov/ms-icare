package com.icare.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelRequest {
    private String name;
    private Integer adLimit;
    private Double price;
}