package com.icare.util.validation;

import com.icare.util.annotation.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidation implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Phone number cannot be blank").addConstraintViolation();
            return false;
        }

        if (!phoneNumber.matches("^\\+994\\d{9}$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Phone number must be in the format +994XXXXXXXXX").addConstraintViolation();
            return false;
        }

        return true;
    }
}