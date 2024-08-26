package com.icare.mapper;

import com.icare.dto.jsonB.Period;
import com.icare.entity.ProductEntity;
import com.icare.dto.request.ProductRequest;
import com.icare.dto.response.ProductImageResponse;
import com.icare.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "request.categoryId", target = "category.id")
    @Mapping(source = "request.cityId", target = "city.id")
    ProductEntity requestToEntity(ProductRequest request);

    @Mapping(source = "entity.category.id", target = "categoryId")
    @Mapping(source = "entity.city.id", target = "cityId")
    ProductResponse entityToResponse(ProductEntity entity, List<ProductImageResponse> images, List<Period> rentalPeriods);

    List<ProductResponse> entitiesToResponses(List<ProductEntity> entities);

    void mapRequestToEntity(@MappingTarget ProductEntity productEntity, ProductRequest request);
}
