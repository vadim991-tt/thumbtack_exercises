package net.thumbtack.school.notes.validator.author;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<ValidLogin, String> {

    @Value("${max_name_length}")
    private int max_name_length;

    @Override
    public void initialize(ValidLogin constraintAnnotation) {

    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        String regex = String.format("[A-Za-zА-Яа-я0-9]{0,%s}", max_name_length);
        return login != null && login.matches(regex);
    }
}