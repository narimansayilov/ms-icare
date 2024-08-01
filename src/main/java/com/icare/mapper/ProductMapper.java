package com.icare.mapper;

import com.icare.dao.entity.ProductEntity;
import com.icare.model.dto.request.ProductRequest;
import com.icare.model.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "request.userId", target = "user.id")
    @Mapping(source = "request.categoryId", target = "category.id")
    @Mapping(source = "request.cityId", target = "city.id")
    ProductEntity requestToEntity(ProductRequest request);

    ProductResponse entityToResponse(ProductEntity entity);

    List<ProductResponse> entitiesToResponses(List<ProductEntity> entities);

    void mapRequestToEntity(@MappingTarget ProductEntity productEntity, ProductRequest request);
}
