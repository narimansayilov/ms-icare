package com.icare.mapper;

import com.icare.entity.CityEntity;
import com.icare.dto.request.CityRequest;
import com.icare.dto.response.CityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CityMapper {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    CityEntity requestToEntity(CityRequest cityRequest);

    CityResponse entityToResponse(CityEntity cityEntity);

    List<CityResponse> entitiesToResponses(Page<CityEntity> entities);

    void mapRequestToEntity(@MappingTarget CityEntity cityEntity, CityRequest cityRequest);
}
