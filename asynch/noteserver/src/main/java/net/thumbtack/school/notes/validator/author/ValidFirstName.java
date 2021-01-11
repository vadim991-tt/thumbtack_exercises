package net.thumbtack.school.notes.validator.author;

import net.thumbtack.school.notes.base.ServerErrorCode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FirstNameValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFirstName {
    String message() default "Invalid firstname";

    ServerErrorCode errorCode() default ServerErrorCode.INVALID_FIRSTNAME;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
