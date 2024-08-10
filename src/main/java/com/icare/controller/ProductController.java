package com.icare.controller;

import com.icare.model.dto.criteria.ProductCriteriaRequest;
import com.icare.model.dto.request.ProductRequest;
import com.icare.model.dto.response.ProductResponse;
import com.icare.service.ProductService;
import com.icare.util.annotation.ValidImages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestPart("request") @Valid ProductRequest request,
                           @RequestPart("images") @ValidImages List<MultipartFile> images){
        productService.addProduct(request, images);
    }

    @GetMapping("/all")
    public List<ProductResponse> getAllProducts(Pageable pageable,
                                                ProductCriteriaRequest request){
        return productService.getAllProducts(pageable, request);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @GetMapping("/my")
    public List<ProductResponse> getMyProducts(Pageable pageable,
                                               @RequestParam(required = false) Boolean status){
        return productService.getMyProducts(pageable, status);
    }

    @PutMapping("/{id}/update")
    public ProductResponse updateProduct(@PathVariable Long id,
                                         @RequestPart("request") @Valid ProductRequest request,
                                         @RequestPart("images") @ValidImages List<MultipartFile> images){
        return productService.editById(id, request, images);
    }

    @PatchMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateProduct(@PathVariable Long id){
        productService.activateProduct(id);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateProduct(@PathVariable Long id){
        productService.deactivateProduct(id);
    }
}