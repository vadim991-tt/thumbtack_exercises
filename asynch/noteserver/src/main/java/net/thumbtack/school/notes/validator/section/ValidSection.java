package net.thumbtack.school.notes.validator.section;

import net.thumbtack.school.notes.base.ServerErrorCode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SectionValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSection {

    ServerErrorCode errorCode() default ServerErrorCode.INVALID_SECTION;

    String message() default "Request contains invalid fields";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
