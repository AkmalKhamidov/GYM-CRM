package com.epamlearning.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidDateFormatValidator.class)
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface ValidDateFormat {
    String message() default "Invalid date format. Please use the format yyyy-MM-dd.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String pattern();
}
