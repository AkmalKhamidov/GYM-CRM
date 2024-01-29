package com.epamlearning.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ValidDateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {

    private String pattern;

    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext context) {
        if ( object == null ) {
            return true;
        }
        try {
            LocalDateTime.parse(object, DateTimeFormatter.ofPattern(pattern));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}