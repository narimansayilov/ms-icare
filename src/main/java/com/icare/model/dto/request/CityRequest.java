package com.icare.model.dto.request;

import com.icare.dao.entity.Translation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CityRequest {
    private String name;
    private List<Translation> translations;
}
