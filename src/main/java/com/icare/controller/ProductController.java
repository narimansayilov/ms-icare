package com.icare.controller;

import com.icare.model.dto.request.ProductRequest;
import com.icare.model.dto.response.ProductResponse;
import com.icare.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        String email = getCurrentUsername();
        productService.addProduct(request, images, email);
    }

    @GetMapping("/{id}/details")
    public ProductResponse getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @PutMapping("/{id}/update")
    public ProductResponse updateProduct(@PathVariable Long id,
                                         @RequestPart("request") ProductRequest request,
                                         @RequestPart("images") MultipartFile[] images){
        String email = getCurrentUsername();
        return productService.editById(id, request, images, email);
    }

    @PatchMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateProduct(@PathVariable Long id){
        String email = getCurrentUsername();
        productService.activateProduct(id, email);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateProduct(@PathVariable Long id){
        String email = getCurrentUsername();
        productService.deactivateProduct(id, email);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}