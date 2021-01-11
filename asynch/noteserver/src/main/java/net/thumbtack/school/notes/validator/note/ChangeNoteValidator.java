package net.thumbtack.school.notes.validator.note;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ChangeNoteValidator implements ConstraintValidator<ValidChangeNoteRequest, Object> {

    @Override
    public void initialize(ValidChangeNoteRequest constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String body = (String) new BeanWrapperImpl(o).getPropertyValue("body");
        Integer sectionId = (Integer) new BeanWrapperImpl(o).getPropertyValue("sectionId");
        return body != null || sectionId != null;
    }

}
