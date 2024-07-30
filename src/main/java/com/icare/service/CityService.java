package com.icare.service;

import com.icare.dao.entity.CityEntity;
import com.icare.dao.repository.CityRepository;
import com.icare.mapper.CityMapper;
import com.icare.model.dto.request.CityRequest;
import com.icare.model.dto.response.CityResponse;
import com.icare.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public void save(CityRequest request){
        log.info("ActionLog.save.start for category name is {}", request.getName());
        cityRepository.save(CityMapper.INSTANCE.requestToEntity(request));
        log.info("ActionLog.save.end for category name is {}", request.getName());
    }

    public List<CityResponse> getAll(){
        return CityMapper.INSTANCE.entitiesToResponses(cityRepository.findAll());
    }

    public CityResponse getById(Long id){
        CityEntity entity = cityRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getById.NotFoundException for category id = {}", id);
            return new NotFoundException("CITY_NOT_FOUND");
        });
        return CityMapper.INSTANCE.entityToResponse(entity);
    }

    public CityResponse editById(Long id, CityRequest request){
        log.info("ActionLog.editById.start for category id = {}", id);
        CityEntity entity = cityRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.editById.NotFoundException for category id = {}", id);
            return new NotFoundException("CITY_NOT_FOUND");
        });
        CityMapper.INSTANCE.mapRequestToEntity(entity, request);
        log.info("ActionLog.editById.end for category id = {}", id);
        return CityMapper.INSTANCE.entityToResponse(entity);
    }

    public void deleteById(Long id){
        log.info("ActionLog.deleteById.start for category id = {}", id);
        // check product count
        cityRepository.deleteById(id);
        log.info("ActionLog.deleteById.end for category id = {}", id);
    }
}
