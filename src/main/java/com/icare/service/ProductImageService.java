package com.icare.service;

import com.icare.dao.repository.ProductImageRepository;
import com.icare.mapper.ProductImageMapper;
import com.icare.model.dto.request.ProductImageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final AmazonS3Service amazonS3Service;
    private final ProductImageRepository productImageRepository;

    public void addImages(MultipartFile[] images, Long productId) {
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
}

