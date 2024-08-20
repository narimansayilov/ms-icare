package com.icare.dto.response;

import com.icare.dto.jsonB.Translation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CityResponse {
    private Long id;
    private String name;
    private Integer productCount;
    private List<Translation> translations;
}