package com.icare.mapper;

import com.icare.dao.entity.CategoryEntity;
import com.icare.model.dto.request.CategoryRequest;
import com.icare.model.dto.response.CategoryResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "parentId", target = "parent.id")
    CategoryEntity requestToEntity(CategoryRequest categoryRequest);

    @Mapping(source = "parent.id", target = "parentId")
    CategoryResponse entityToResponse(CategoryEntity categoryEntity);

    List<CategoryResponse> entitiesToResponses(List<CategoryEntity> categories);

    void mapRequestToEntity(@MappingTarget CategoryEntity categoryEntity, CategoryRequest categoryRequest);
}