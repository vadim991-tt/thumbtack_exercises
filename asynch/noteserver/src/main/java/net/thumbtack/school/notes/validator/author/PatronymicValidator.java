package net.thumbtack.school.notes.validator.author;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatronymicValidator implements ConstraintValidator<ValidPatronymic, String> {

    @Value("${max_name_length}")
    private int max_name_length;

    @Override
    public void initialize(ValidPatronymic constraintAnnotation) {

    }

    @Override
    public boolean isValid(String patronymic, ConstraintValidatorContext constraintValidatorContext) {
        String regex = String.format("[A-Za-zА-Яа-я-]{0,%s}", max_name_length);
        return patronymic == null || patronymic.matches(regex);
    }
}