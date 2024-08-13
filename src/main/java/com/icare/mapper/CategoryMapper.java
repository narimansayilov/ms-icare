package com.icare.mapper;

import com.icare.entity.CategoryEntity;
import com.icare.dto.request.CategoryRequest;
import com.icare.dto.response.CategoryResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "parentId", target = "parent.id")
    CategoryEntity requestToEntity(CategoryRequest categoryRequest);

    @Mapping(source = "parent.id", target = "parentId")
    CategoryResponse entityToResponse(CategoryEntity categoryEntity);

    List<CategoryResponse> entitiesToResponses(Page<CategoryEntity> categories);

    void mapRequestToEntity(@MappingTarget CategoryEntity categoryEntity, CategoryRequest categoryRequest);
}