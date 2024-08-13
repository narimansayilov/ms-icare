package com.icare.mapper;

import com.icare.entity.LevelEntity;
import com.icare.dto.request.LevelRequest;
import com.icare.dto.response.LevelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LevelMapper {
    LevelMapper INSTANCE = Mappers.getMapper(LevelMapper.class);

    LevelEntity requestToEntity(LevelRequest request);

    LevelResponse entityToResponse(LevelEntity entity);

    List<LevelResponse> entitiesToResponses(Page<LevelEntity> entities);

    void mapRequestToEntity(@MappingTarget LevelEntity entity, LevelRequest request);
}
