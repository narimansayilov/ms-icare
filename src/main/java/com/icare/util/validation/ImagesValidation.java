package com.icare.util.validation;

import com.icare.util.annotation.ValidImages;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

public class ImagesValidation implements ConstraintValidator<ValidImages, List<MultipartFile>> {

    private long minSize;
    private long maxSize;
    private int minCount;
    private int maxCount;
    private Set<String> allowedFormats;

    @Override
    public void initialize(ValidImages constraintAnnotation) {
        this.minSize = constraintAnnotation.minSize();
        this.maxSize = constraintAnnotation.maxSize();
        this.minCount = constraintAnnotation.minCount();
        this.maxCount = constraintAnnotation.maxCount();
        this.allowedFormats = new HashSet<>(Arrays.asList(constraintAnnotation.allowedFormats()));
    }

    @Override
    public boolean isValid(List<MultipartFile> images, ConstraintValidatorContext context) {
        if (images == null || images.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Image list cannot be null or empty").addConstraintViolation();
            return false;
        }

        if (images.size() < minCount || images.size() > maxCount) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("Image count must be between %d and %d", minCount, maxCount)).addConstraintViolation();
            return false;
        }

        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Image cannot be empty").addConstraintViolation();
                return false;
            }

            if (image.getSize() < minSize || image.getSize() > maxSize) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(String.format("Image size must be between %d bytes and %d bytes", minSize, maxSize)).addConstraintViolation();
                return false;
            }

            if (!allowedFormats.contains(image.getContentType())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Invalid image format. Allowed formats are: " + String.join(", ", allowedFormats)).addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
