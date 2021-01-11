package net.thumbtack.school.notes.validator.author;

import net.thumbtack.school.notes.base.ServerErrorCode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword  {
    String message() default "Incorrect password";

    ServerErrorCode errorCode() default ServerErrorCode.INVALID_PASSWORD;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
