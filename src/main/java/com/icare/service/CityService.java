package com.icare.service;

import com.icare.entity.CityEntity;
import com.icare.repository.CityRepository;
import com.icare.mapper.CityMapper;
import com.icare.dto.request.CityRequest;
import com.icare.dto.response.CityResponse;
import com.icare.exception.DeletionException;
import com.icare.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public void save(CityRequest request){
        cityRepository.save(CityMapper.INSTANCE.requestToEntity(request));
    }

    public List<CityResponse> getAllCities(Pageable pageable){
        Page<CityEntity> entities = cityRepository.findAll(pageable);
        return CityMapper.INSTANCE.entitiesToResponses(entities);
    }

    public CityResponse getById(Long id){
        CityEntity entity = cityRepository.findById(id).orElseThrow(() ->
            new NotFoundException("CITY_NOT_FOUND"));
        return CityMapper.INSTANCE.entityToResponse(entity);
    }

    public CityResponse editById(Long id, CityRequest request){
        CityEntity entity = cityRepository.findById(id).orElseThrow(() ->
            new NotFoundException("CITY_NOT_FOUND"));
        CityMapper.INSTANCE.mapRequestToEntity(entity, request);
        return CityMapper.INSTANCE.entityToResponse(entity);
    }

    public void deleteById(Long id){
        CityEntity entity = cityRepository.findById(id).orElseThrow(() ->
                new NotFoundException("CITY_NOT_FOUND"));
        if(entity.getProductCount() > 0 )
            throw new DeletionException("CITY_CANNOT_BE_DELETED");
        else
            cityRepository.deleteById(id);
    }
}