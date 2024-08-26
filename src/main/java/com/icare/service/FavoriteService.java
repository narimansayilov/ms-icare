package com.icare.service;

import com.icare.dto.response.FavoriteResponse;
import com.icare.dto.response.ProductResponse;
import com.icare.entity.FavoriteEntity;
import com.icare.entity.ProductEntity;
import com.icare.entity.UserEntity;
import com.icare.exception.NotFoundException;
import com.icare.mapper.FavoriteMapper;
import com.icare.mapper.ProductMapper;
import com.icare.repository.FavoriteRepository;
import com.icare.repository.ProductRepository;
import com.icare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final UserService userService;

    public void addFavorite(Long productId) {
        String email = userService.getCurrentEmail();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        ProductEntity product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("PRODUCT_NOT_FOUND"));
        favoriteRepository.save(FavoriteMapper.INSTANCE.requestToEntity(product.getId(), user.getId()));
    }

    public List<FavoriteResponse> getFavorite(Pageable pageable) {
        String email = userService.getCurrentEmail();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        Page<FavoriteEntity> favoriteEntities = favoriteRepository.findByUserId(user.getId(), pageable);
        return favoriteEntities.stream().map(entity -> {
            FavoriteResponse response = FavoriteMapper.INSTANCE.entityToResponse(entity);
            response.setProduct(productService.getProduct(entity.getProduct().getId()));
            return response;
        }).toList();
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
