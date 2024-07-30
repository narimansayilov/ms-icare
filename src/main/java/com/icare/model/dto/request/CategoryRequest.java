package com.icare.model.dto.request;

import com.icare.dao.entity.Translation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryRequest {
    private String name;
    private boolean parentCategory;
    private Integer parentId;
    private List<Translation> translations;
}
