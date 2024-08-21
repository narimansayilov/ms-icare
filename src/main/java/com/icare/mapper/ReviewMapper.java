package com.icare.mapper;

import com.icare.dto.request.ReviewRequest;
import com.icare.entity.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "reviewRequest.rentalId", target = "rental.id")
    ReviewEntity requestToEntity(ReviewRequest reviewRequest, Long userId, Long productId);
}
