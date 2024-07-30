package com.icare.service;

import com.icare.dao.entity.CategoryEntity;
import com.icare.dao.repository.CategoryRepository;
import com.icare.mapper.CategoryMapper;
import com.icare.model.dto.request.CategoryRequest;
import com.icare.model.dto.response.CategoryResponse;
import com.icare.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void save(CategoryRequest request) {
        log.info("ActionLog.save.start for category name is {}", request.getName());
        categoryRepository.save(CategoryMapper.INSTANCE.requestToEntity(request));
        log.info("ActionLog.save.end for category name is {}", request.getName());
    }

    public List<CategoryResponse> getAll() {
        return CategoryMapper.INSTANCE.entitiesToResponses(categoryRepository.findAll());
    }

    public CategoryResponse getById(Long id) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() -> {
                log.error("ActionLog.getById.NotFoundException for category id = {}", id);
                return new NotFoundException("CATEGORY_NOT_FOUND");
        });
        return CategoryMapper.INSTANCE.entityToResponse(entity);
    }

    public CategoryResponse editById(Long id, CategoryRequest request) {
        log.info("ActionLog.editById.start for category id = {}", id);
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.editById.NotFoundException for category id = {}", id);
            return new NotFoundException("CATEGORY_NOT_FOUND");
        });
        CategoryMapper.INSTANCE.mapRequestToEntity(entity, request);
        categoryRepository.save(entity);
        log.info("ActionLog.editById.end for category id = {}", id);
        return CategoryMapper.INSTANCE.entityToResponse(entity);
    }

    public void deleteById(Long id) {
        log.info("ActionLog.deleteById.start for category id = {}", id);
        // check product count
        categoryRepository.deleteById(id);
        log.info("ActionLog.deleteById.end for category id = {}", id);
    }
}
