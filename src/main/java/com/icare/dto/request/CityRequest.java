package com.icare.dto.request;

import com.icare.entity.Translation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CityRequest {
    private String name;
    private List<Translation> translations;
}