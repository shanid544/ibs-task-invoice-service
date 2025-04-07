package com.ibs_demo.invoice_service.annotations.validator;

import com.ibs_demo.invoice_service.annotations.EnumValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, Enum<?>> {
    private EnumValidator annotation;

    @Override
    public void initialize(EnumValidator annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) return false;

        return Arrays.stream(annotation.enumClass().getEnumConstants())
                .anyMatch(e -> e.name().equals(value.name()));
    }
}
