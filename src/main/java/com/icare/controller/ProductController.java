package com.icare.controller;

import com.icare.model.dto.request.ProductRequest;
import com.icare.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestPart("request") ProductRequest request,
                           @RequestPart("images") MultipartFile[] images){
        productService.addProduct(request, images);
    }
}

