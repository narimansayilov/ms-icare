package com.icare.mapper;

import com.icare.dao.entity.UserEntity;
import com.icare.model.dto.request.UserRegisterRequest;
import com.icare.model.dto.request.UserUpdateRequest;
import com.icare.model.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity registerRequestToEntity(UserRegisterRequest request);

    UserResponse entityToResponse(UserEntity entity);

    // List<UserResponse> entitiesToResponses(List<UserEntity> entities);

    void mapRequestToEntity(@MappingTarget UserEntity entity, UserUpdateRequest request);
}
