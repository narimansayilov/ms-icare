package com.icare.service;

import com.icare.entity.LevelEntity;
import com.icare.repository.LevelRepository;
import com.icare.mapper.LevelMapper;
import com.icare.dto.request.LevelRequest;
import com.icare.dto.response.LevelResponse;
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
public class LevelService {
    private final LevelRepository levelRepository;

    public void addLevel(LevelRequest request) {
        LevelEntity entity = LevelMapper.INSTANCE.requestToEntity(request);
        levelRepository.save(entity);
    }

    public LevelResponse getLevel(Long id) {
        LevelEntity entity = levelRepository.findById(id).orElseThrow(() ->
                new NotFoundException("LEVEL_NOT_FOUND"));
        return LevelMapper.INSTANCE.entityToResponse(entity);
    }

    public List<LevelResponse> getAllLevels(Pageable pageable) {
        Page<LevelEntity> entities = levelRepository.findAll(pageable);
        return LevelMapper.INSTANCE.entitiesToResponses(entities);
    }

    public LevelResponse updateLevel(Long id, LevelRequest request) {
        LevelEntity entity = levelRepository.findById(id).orElseThrow(() ->
                new NotFoundException("LEVEL_NOT_FOUND"));
        LevelMapper.INSTANCE.mapRequestToEntity(entity, request);
        levelRepository.save(entity);
        return LevelMapper.INSTANCE.entityToResponse(entity);
    }

    public void deleteLevel(Long id) {
        LevelEntity entity = levelRepository.findById(id).orElseThrow(() ->
                new NotFoundException("LEVEL_NOT_FOUND"));
        if(entity.getUserCount() > 0)
            throw new DeletionException("LEVEL_CANNOT_BE_DELETED");
        else
            levelRepository.deleteById(id);
    }
}