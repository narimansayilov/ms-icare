package com.icare.util.annotation;

import com.icare.util.validation.PhoneNumberValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidation.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "Invalid phone number.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}