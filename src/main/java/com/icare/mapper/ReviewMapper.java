package com.icare.mapper;

import com.icare.dto.request.ReviewRequest;
import com.icare.dto.response.ReviewResponse;
import com.icare.dto.response.UserResponse;
import com.icare.entity.ReviewEntity;
import com.icare.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "reviewRequest.rentalId", target = "rental.id")
    ReviewEntity requestToEntity(ReviewRequest reviewRequest, Long userId, Long productId);

    @Mapping(source = "product.id", target = "productId")
    ReviewResponse entityToResponse(ReviewEntity entity);

    @Mapping(source = "reviewRequest.rentalId", target = "reviewEntity.rental.id")
    void mapRequestToEntity(@MappingTarget ReviewEntity reviewEntity, ReviewRequest reviewRequest);
}
