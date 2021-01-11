package net.thumbtack.school.spring.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RecordingWithLinkValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordingWithLink {
    String message() default "Recording doesn't contain any link";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
