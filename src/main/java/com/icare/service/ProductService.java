package com.icare.service;

import com.icare.dao.entity.*;
import com.icare.dao.repository.*;
import com.icare.mapper.ProductMapper;
import com.icare.model.dto.request.ProductRequest;
import com.icare.model.exception.LimitExceededException;
import com.icare.model.exception.NotActiveException;
import com.icare.model.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageService productImageService;
    private final LevelRepository levelRepository;

    @Transactional
    public void addProduct(ProductRequest request, MultipartFile[] images){
        log.info("ActionLog.addProducts.start for product title is {}", request.getTitle());
        ProductEntity entity = ProductMapper.INSTANCE.requestToEntity(request);
        productRepository.save(entity);
        productImageService.addImages(images, entity.getId());
        setCount(request.getUserId(), request.getCategoryId(), request.getCityId());
        log.info("ActionLog.addProducts.end for product title is {}", entity.getTitle());
    }

    private void setCount(Long userId, Long categoryId, Long cityId){
        UserEntity userEntity = getUser(userId);
        userEntity.setProductCount(userEntity.getProductCount() + 1);
        userRepository.save(userEntity);

        CategoryEntity categoryEntity = getCategory(categoryId);
        categoryEntity.setProductCount(categoryEntity.getProductCount() + 1);
        categoryRepository.save(categoryEntity);

        CityEntity cityEntity = getCity(cityId);
        cityEntity.setProductCount(cityEntity.getProductCount() + 1);
        cityRepository.save(cityEntity);
    }

    private UserEntity getUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> {
            log.error("ActionLog.getUser.NotFoundException user id = {}", userId);
            return new NotFoundException("USER_NOT_FOUND");
        });
        if (!userEntity.getStatus()) {
            log.error("ActionLog.getUser.NotActiveException user id = {}", userId);
            throw new NotActiveException("USER_NOT_ACTIVE");
        }
        LevelEntity level = levelRepository.findById(userEntity.getLevel().getId()).orElseThrow(() -> {
            log.error("ActionLog.getUser.NotFoundException level id = {}", userEntity.getLevel().getId());
            return new NotFoundException("LEVEL_NOT_FOUND");
        });
        if (level.getAdLimit() <= userEntity.getProductCount()) {
            log.error("ActionLog.getUser.LimitExceededException user id = {}", userId);
            throw new LimitExceededException("AD_LIMIT_EXCEEDED");
        }
        return userEntity;
    }

    private CategoryEntity getCategory(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() -> {
            log.error("ActionLog.getCategory.NotFoundException category id = {}", categoryId);
            return new NotFoundException("CATEGORY_NOT_FOUND");
        });
    }

    private CityEntity getCity(Long cityId){
        return cityRepository.findById(cityId).orElseThrow(() -> {
            log.error("ActionLog.getCity.NotFoundException city id = {}", cityId);
            return new NotFoundException("CITY_NOT_FOUND");
        });
    }
}
