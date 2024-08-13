package com.icare.service;

import com.icare.entity.ProductImageEntity;
import com.icare.repository.ProductImageRepository;
import com.icare.mapper.ProductImageMapper;
import com.icare.dto.request.ProductImageRequest;
import com.icare.dto.response.ProductImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final AmazonS3Service amazonS3Service;
    private final ProductImageRepository productImageRepository;

    public void addImages(List<MultipartFile> images, Long productId) {
        boolean main = true;
        for (MultipartFile image : images) {
            ProductImageRequest productImageRequest = ProductImageRequest.builder()
                    .productId(productId)
                    .imageUrl(amazonS3Service.uploadFile(image))
                    .main(main)
                    .build();
            productImageRepository.save(ProductImageMapper.INSTANCE.requestToEntity(productImageRequest));
            main = !main;
        }
    }

    public void editImages(List<MultipartFile> images, Long productId) {
        List<ProductImageEntity> productImages = productImageRepository
                .findByProductIdAndStatus(productId, true);
        productImages.forEach(entity -> entity.setStatus(false));
        productImageRepository.saveAll(productImages);
        addImages(images, productId);
    }

    public List<ProductImageResponse> getImages(Long productId) {
        List<ProductImageEntity> entities = productImageRepository
                .findByProductIdAndStatus(productId, true);
        return ProductImageMapper.INSTANCE.entitiesToResponses(entities);
    }
}