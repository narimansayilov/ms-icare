package com.icare.mapper;

import com.icare.dto.request.RoleRequest;
import com.icare.dto.response.RoleResponse;
import com.icare.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleEntity requestToEntity(RoleRequest roleRequest);

    RoleResponse entityToResponse(RoleEntity roleEntity);

    List<RoleResponse> entitiesToResponses(List<RoleEntity> entities);

    void mapRequestToEntity(@MappingTarget RoleEntity roleEntity, RoleRequest roleRequest);
}
