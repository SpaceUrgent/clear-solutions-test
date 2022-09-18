package com.test.userapp.lib;

import java.time.LocalDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {
    @Value("${user.min.age}")
    private int minAge;

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date == null ? true : LocalDate.now().minusYears(minAge).isAfter(date);
    }
}
