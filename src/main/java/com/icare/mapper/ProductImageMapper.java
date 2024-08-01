package com.icare.mapper;

import com.icare.dao.entity.ProductImageEntity;
import com.icare.model.dto.request.ProductImageRequest;
import com.icare.model.dto.response.ProductImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductImageMapper {
    ProductImageMapper INSTANCE = Mappers.getMapper(ProductImageMapper.class);

    @Mapping(target = "product.id", source = "request.productId")
    ProductImageEntity requestToEntity(ProductImageRequest request);

    @Mapping(target = "url", source = "imageUrl")
    ProductImageResponse entityToResponse(ProductImageEntity entity);

    List<ProductImageResponse> entitiesToResponses(List<ProductImageEntity> entities);
}
