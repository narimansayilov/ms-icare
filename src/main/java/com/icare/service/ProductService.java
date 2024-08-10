package com.icare.service;

import com.icare.dao.entity.*;
import com.icare.dao.repository.*;
import com.icare.mapper.ProductMapper;
import com.icare.model.dto.criteria.ProductCriteriaRequest;
import com.icare.model.dto.request.ProductRequest;
import com.icare.model.dto.response.ProductImageResponse;
import com.icare.model.dto.response.ProductResponse;
import com.icare.model.exception.*;
import com.icare.service.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageService productImageService;
    private final LevelRepository levelRepository;

    @Transactional
    public void addProduct(ProductRequest request, List<MultipartFile> images){
        log.info("ActionLog.addProducts.start for product title is {}", request.getTitle());
        ProductEntity entity = ProductMapper.INSTANCE.requestToEntity(request);
        String email = userService.getCurrentUsername();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("ActionLog.addProducts.NotFoundException for user email {}", email);
            return new  NotFoundException("USER_NOT_FOUND");
        });
        entity.setUser(user);
        productRepository.save(entity);
        productImageService.addImages(images, entity.getId());
        setCount(user.getId(), request.getCategoryId(), request.getCityId());
        log.info("ActionLog.addProducts.end for product title is {}", entity.getTitle());
    }

    public ProductResponse getProduct(Long id){
        log.info("ActionLog.addProducts.start for product id is {}", id);
        ProductEntity entity = productRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getProduct.NotFoundException for product id = {}", id);
            return new NotFoundException("PRODUCT_NOT_FOUND");
        });
        List<ProductImageResponse> images = productImageService.getImages(id);
        ProductResponse response = ProductMapper.INSTANCE.entityToResponse(entity, images);
        log.info("ActionLog.addProducts.end for product id is {}", id);
        return response;
    }

    public List<ProductResponse> getAllProducts(Pageable pageable, ProductCriteriaRequest request){
        Specification<ProductEntity> specification = ProductSpecification.getProductByCriteria(request);
        return getProducts(specification, pageable);
    }

    public List<ProductResponse> getMyProducts(Pageable pageable, Boolean status){
        String email = userService.getCurrentUsername();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("ActionLog.getMyProducts.NotFoundException for user email = {}", email);
            return new NotFoundException("USER_NOT_FOUND");
        });
        Specification<ProductEntity> specification = ProductSpecification.getProductByUserAndStatus(user.getId(), status);
        return getProducts(specification, pageable);
    }


    @Transactional
    public ProductResponse editById(Long id, ProductRequest request, List<MultipartFile> images){
        log.info("ActionLog.editById.start for product title is {}", request.getTitle());
        ProductEntity entity = productRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.editById.NotFoundException for product id = {}", id);
            return new NotFoundException("PRODUCT_NOT_FOUND");
        });
        if(!entity.getUser().getEmail().equals(userService.getCurrentUsername())){
            log.error("ActionLog.editById.UnauthorizedAccessException for product id = {}", id);
            throw new UnauthorizedAccessException("UNAUTHORIZED_ACCESS");
        }
        if(!entity.getStatus()){
            log.error("ActionLog.editById.NotActiveException for product id = {}", id);
            throw new NotActiveException("PRODUCT_NOT_ACTIVE");
        }
        ProductMapper.INSTANCE.mapRequestToEntity(entity, request);
        productRepository.save(entity);
        productImageService.editImages(images, id);
        List<ProductImageResponse> imageResponses = productImageService.getImages(id);
        return ProductMapper.INSTANCE.entityToResponse(entity, imageResponses);
    }

    public void activateProduct(Long id){
        log.info("ActionLog.activateProduct.start for product id is {}", id);
        ProductEntity entity = productRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.activateProduct.NotFoundException for product id = {}", id);
            return new NotFoundException("PRODUCT_NOT_FOUND");
        });
        if(!entity.getUser().getEmail().equals(userService.getCurrentUsername())){
            log.error("ActionLog.activateProduct.UnauthorizedAccessException for product id = {}", id);
            throw new UnauthorizedAccessException("UNAUTHORIZED_ACCESS");
        }
        if(entity.getStatus()){
            log.error("ActionLog.activateProduct.ActiveException for product id = {}", id);
            throw new ActiveException("PRODUCT_ACTIVE");
        }
        entity.setStatus(true);
        productRepository.save(entity);
    }

    public void deactivateProduct(Long id){
        log.info("ActionLog.deactivateProduct.start for product id is {}", id);
        ProductEntity entity = productRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.deactivateProduct.NotFoundException for product id = {}", id);
            return new NotFoundException("PRODUCT_NOT_FOUND");
        });
        if(!entity.getUser().getEmail().equals(userService.getCurrentUsername())){
            log.error("ActionLog.deactivateProduct.UnauthorizedAccessException for product id = {}", id);
            throw new UnauthorizedAccessException("UNAUTHORIZED_ACCESS");
        }
        if(!entity.getStatus()){
            log.error("ActionLog.deactivateProduct.NotActiveException for product id = {}", id);
            throw new NotActiveException("PRODUCT_NOT_ACTIVE");
        }
        entity.setStatus(false);
        productRepository.save(entity);
    }

    private List<ProductResponse> getProducts(Specification<ProductEntity> specification, Pageable pageable){
        Page<ProductEntity> entities = productRepository.findAll(specification, pageable);
        List<ProductResponse> responses = new ArrayList<>();
        for(ProductEntity entity : entities){
            responses.add(getProduct(entity.getId()));
        }
        return responses;
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