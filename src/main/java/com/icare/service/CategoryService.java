package com.icare.service;

import com.icare.entity.CategoryEntity;
import com.icare.repository.CategoryRepository;
import com.icare.mapper.CategoryMapper;
import com.icare.dto.request.CategoryRequest;
import com.icare.dto.response.CategoryResponse;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void save(CategoryRequest request) {
        CategoryEntity entity = CategoryMapper.INSTANCE.requestToEntity(request);
        if(!request.getParentCategory()){
            CategoryEntity parent = categoryRepository.findById(request.getParentId()).orElseThrow(() ->
                    new NotFoundException("CATEGORY_NOT_FOUND"));
            parent.setSubCategoryCount(parent.getSubCategoryCount() + 1);
            categoryRepository.save(parent);
            entity.setParent(parent);
        }
        categoryRepository.save(entity);
    }

    public List<CategoryResponse> getAllCategories(Pageable pageable) {
        Page<CategoryEntity> entities = categoryRepository.findAll(pageable);
        return CategoryMapper.INSTANCE.entitiesToResponses(entities);
    }

    public CategoryResponse getById(Long id) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("CATEGORY_NOT_FOUND"));
        return CategoryMapper.INSTANCE.entityToResponse(entity);
    }

    public CategoryResponse editById(Long id, CategoryRequest request) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() ->
             new NotFoundException("CATEGORY_NOT_FOUND"));
        CategoryMapper.INSTANCE.mapRequestToEntity(entity, request);
        categoryRepository.save(entity);
        return CategoryMapper.INSTANCE.entityToResponse(entity);
    }

    public void deleteById(Long id) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("CATEGORY_NOT_FOUND"));
        if(entity.getProductCount() > 0 || entity.getSubCategoryCount() > 0)
            throw new DeletionException("CATEGORY_CANNOT_BE_DELETED");
        else
            categoryRepository.deleteById(id);
    }
}