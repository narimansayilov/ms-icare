package com.icare.service;

import com.icare.dto.criteria.ProductCriteriaRequest;
import com.icare.dto.request.ProductRequest;
import com.icare.dto.response.ProductImageResponse;
import com.icare.dto.response.ProductResponse;
import com.icare.entity.*;
import com.icare.exception.*;
import com.icare.mapper.ProductMapper;
import com.icare.repository.*;
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
        ProductEntity entity = ProductMapper.INSTANCE.requestToEntity(request);
        String email = userService.getCurrentEmail();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        entity.setUser(user);
        productRepository.save(entity);
        productImageService.addImages(images, entity.getId());
        setCount(user.getId(), request.getCategoryId(), request.getCityId());
    }

    public ProductResponse getProduct(Long id){
        ProductEntity entity = productRepository.findById(id).orElseThrow(() ->
                new NotFoundException("PRODUCT_NOT_FOUND"));
        List<ProductImageResponse> images = productImageService.getImages(id);
        return ProductMapper.INSTANCE.entityToResponse(entity, images);
    }

    public List<ProductResponse> getAllProducts(Pageable pageable, ProductCriteriaRequest request){
        Specification<ProductEntity> specification = ProductSpecification.getProductByCriteria(request);
        return getProducts(specification, pageable);
    }

    public List<ProductResponse> getMyProducts(Pageable pageable, Boolean status){
        String email = userService.getCurrentEmail();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        Specification<ProductEntity> specification = ProductSpecification.getProductByUserAndStatus(user.getId(), status);
        return getProducts(specification, pageable);
    }

    @Transactional
    public ProductResponse editById(Long id, ProductRequest request, List<MultipartFile> images){
        ProductEntity entity = checkAuthorizeAndReturnProduct(id);
        if(!entity.getStatus()){
            throw new NotActiveException("PRODUCT_NOT_ACTIVE");
        }
        ProductMapper.INSTANCE.mapRequestToEntity(entity, request);
        productRepository.save(entity);
        productImageService.editImages(images, id);
        List<ProductImageResponse> imageResponses = productImageService.getImages(id);
        return ProductMapper.INSTANCE.entityToResponse(entity, imageResponses);
    }

    public void activateProduct(Long id){
        ProductEntity entity = checkAuthorizeAndReturnProduct(id);
        if(entity.getStatus()){
            throw new ActiveException("PRODUCT_ACTIVE");
        }
        entity.setStatus(true);
        productRepository.save(entity);
    }

    public void deactivateProduct(Long id){
        ProductEntity entity = checkAuthorizeAndReturnProduct(id);
        if(!entity.getStatus()){
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
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() ->
            new NotFoundException("USER_NOT_FOUND"));
        if (!userEntity.getStatus()) {
            throw new NotActiveException("USER_NOT_ACTIVE");
        }
        LevelEntity level = levelRepository.findById(userEntity.getLevel().getId()).orElseThrow(() ->
            new NotFoundException("LEVEL_NOT_FOUND"));
        if (level.getAdLimit() <= userEntity.getProductCount()) {
            throw new LimitExceededException("AD_LIMIT_EXCEEDED");
        }
        return userEntity;
    }

    private ProductEntity checkAuthorizeAndReturnProduct(Long id){
        ProductEntity entity = productRepository.findById(id).orElseThrow(() ->
                new NotFoundException("PRODUCT_NOT_FOUND"));
        if(!entity.getUser().getEmail().equals(userService.getCurrentEmail())){
            throw new UnauthorizedAccessException("UNAUTHORIZED_ACCESS");
        }
        return entity;
    }

    private CategoryEntity getCategory(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() ->
             new NotFoundException("CATEGORY_NOT_FOUND"));
    }

    private CityEntity getCity(Long cityId){
        return cityRepository.findById(cityId).orElseThrow(() ->
            new NotFoundException("CITY_NOT_FOUND"));
    }
}