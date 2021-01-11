package net.thumbtack.school.spring.validator;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RecordingWithLinkValidator implements ConstraintValidator<RecordingWithLink, Object> {

    @Override
    public void initialize(RecordingWithLink constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String linkToAudioFile = (String) new BeanWrapperImpl(o).getPropertyValue("linkToAudioFile");
        String linkToVideoFile = (String) new BeanWrapperImpl(o).getPropertyValue("linkToAudioFile");
        return linkToAudioFile != null || linkToVideoFile != null;
    }
}
