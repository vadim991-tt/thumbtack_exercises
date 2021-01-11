package net.thumbtack.school.notes.validator.author;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LastNameValidator implements ConstraintValidator<ValidLastName, String> {

    @Value("${max_name_length}")
    private int max_name_length;

    @Override
    public void initialize(ValidLastName constraintAnnotation) {

    }

    @Override
    public boolean isValid(String lastName, ConstraintValidatorContext constraintValidatorContext) {
        String regex = String.format("[A-Za-zА-Яа-я-]{0,%s}", max_name_length);
        return lastName != null && lastName.matches(regex);
    }
}