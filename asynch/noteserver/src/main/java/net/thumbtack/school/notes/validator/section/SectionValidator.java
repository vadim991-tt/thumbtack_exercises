package net.thumbtack.school.notes.validator.section;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SectionValidator implements ConstraintValidator<ValidSection, Object> {

    @Override
    public void initialize(ValidSection constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String name = (String) new BeanWrapperImpl(o).getPropertyValue("name");
        String regex = "[A-Za-zА-Яа-я0-9- ]+";
        return name != null && name.matches(regex);
    }

}
