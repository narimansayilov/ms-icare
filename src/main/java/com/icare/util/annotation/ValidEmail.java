package com.icare.util.annotation;


import com.icare.util.validation.EmailValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidation.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Invalid email address.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}