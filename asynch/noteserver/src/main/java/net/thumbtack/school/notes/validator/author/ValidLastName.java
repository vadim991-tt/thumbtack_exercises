package net.thumbtack.school.notes.validator.author;

import net.thumbtack.school.notes.base.ServerErrorCode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LastNameValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLastName {
    String message() default "Invalid last name";

    ServerErrorCode errorCode() default ServerErrorCode.INVALID_LASTNAME;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
