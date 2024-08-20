package com.icare.mapper;

import com.icare.dto.request.RentalRequest;
import com.icare.entity.RentalEntity;
import com.icare.enums.RentalStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RentalMapper {
    RentalMapper INSTANCE = Mappers.getMapper(RentalMapper.class);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "request.productId", target = "product.id")
    RentalEntity requestToEntity(RentalRequest request, Long orderId, RentalStatus status);
}
