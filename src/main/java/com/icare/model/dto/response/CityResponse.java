package com.icare.model.dto.response;

import com.icare.dao.entity.Translation;
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
