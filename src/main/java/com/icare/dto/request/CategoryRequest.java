package com.icare.dto.request;

import com.icare.entity.Translation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryRequest {
    private String name;
    private Boolean parentCategory;
    private Long parentId;
    private List<Translation> translations;
}