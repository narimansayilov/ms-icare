package com.icare.mapper;

import com.icare.entity.OrderEntity;
import com.icare.entity.UserEntity;
import com.icare.enums.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "user", source = "user")
    OrderEntity requestToEntity(UserEntity user, OrderStatus status);
}
