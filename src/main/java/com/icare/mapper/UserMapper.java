package com.icare.mapper;

import com.icare.entity.UserEntity;
import com.icare.dto.request.UserRegisterRequest;
import com.icare.dto.request.UserUpdateRequest;
import com.icare.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity registerRequestToEntity(UserRegisterRequest request);

    UserResponse entityToResponse(UserEntity entity);

    List<UserResponse> entitiesToResponses(Page<UserEntity> entities);

    void mapRequestToEntity(@MappingTarget UserEntity entity, UserUpdateRequest request);
}
