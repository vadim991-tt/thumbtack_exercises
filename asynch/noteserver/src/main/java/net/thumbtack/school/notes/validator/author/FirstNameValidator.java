package net.thumbtack.school.notes.validator.author;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FirstNameValidator implements ConstraintValidator<ValidFirstName, String> {

    @Value("${max_name_length}")
    private int max_name_length;

    @Override
    public void initialize(ValidFirstName constraintAnnotation) {

    }

    @Override
    public boolean isValid(String firstName, ConstraintValidatorContext constraintValidatorContext) {
        String regex = String.format("[A-Za-zА-Яа-я-]{0,%s}", max_name_length);
        return firstName != null && firstName.matches(regex);
    }
}