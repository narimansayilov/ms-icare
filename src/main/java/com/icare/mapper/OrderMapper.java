package com.icare.mapper;

import com.icare.dto.response.OrderResponse;
import com.icare.dto.response.RentalResponse;
import com.icare.entity.OrderEntity;
import com.icare.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "userId", target = "user.id")
    OrderEntity requestToEntity(Long userId);

    OrderResponse entityToResponse(OrderEntity orderEntity, List<RentalResponse> rentals);

    List<OrderResponse> entitiesToResponses(List<OrderEntity> entities);
}
