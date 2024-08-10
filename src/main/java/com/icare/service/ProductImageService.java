package com.icare.service;

import com.icare.dao.entity.ProductImageEntity;
import com.icare.dao.repository.ProductImageRepository;
import com.icare.mapper.ProductImageMapper;
import com.icare.model.dto.request.ProductImageRequest;
import com.icare.model.dto.response.ProductImageResponse;
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
        log.info("ActionLog.addImages.start for product id = {}", productId);
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
        log.info("ActionLog.addImages.end for product id = {}", productId);
    }

    public void editImages(List<MultipartFile> images, Long productId) {
        log.info("ActionLog.editImages.start for product id = {}", productId);
        List<ProductImageEntity> productImages = productImageRepository.findByProductIdAndStatus(productId, true);
        productImages.forEach(entity -> entity.setStatus(false));
        productImageRepository.saveAll(productImages);
        addImages(images, productId);
        log.info("ActionLog.editImages.end for product id = {}", productId);
    }

    public List<ProductImageResponse> getImages(Long productId) {
        log.info("ActionLog.getImages.start for product id = {}", productId);
        List<ProductImageEntity> entities = productImageRepository.findByProductIdAndStatus(productId, true);
        List<ProductImageResponse> responses = ProductImageMapper.INSTANCE.entitiesToResponses(entities);
        log.info("ActionLog.getImages.end for product id = {}", productId);
        return responses;
    }
}

