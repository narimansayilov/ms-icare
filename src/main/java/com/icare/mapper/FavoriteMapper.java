package com.icare.mapper;

import com.icare.dto.response.FavoriteResponse;
import com.icare.dto.response.ProductResponse;
import com.icare.entity.FavoriteEntity;
import com.icare.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FavoriteMapper {
    FavoriteMapper INSTANCE = Mappers.getMapper(FavoriteMapper.class);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "userId", target = "user.id")
    FavoriteEntity requestToEntity(Long productId, Long userId);

    FavoriteResponse entityToResponse(FavoriteEntity entity);

    List<FavoriteResponse> entitiesToResponses(List<FavoriteEntity> entities);
}
