package com.icare.mapper;

import com.icare.dao.entity.CityEntity;
import com.icare.model.dto.request.CityRequest;
import com.icare.model.dto.response.CityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CityMapper {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    CityEntity requestToEntity(CityRequest cityRequest);

    CityResponse entityToResponse(CityEntity cityEntity);

    List<CityResponse> entitiesToResponses(List<CityEntity> entities);

    void mapRequestToEntity(@MappingTarget CityEntity cityEntity, CityRequest cityRequest);
}
