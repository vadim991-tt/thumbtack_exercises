package net.thumbtack.school.notes.validator.author;

import net.thumbtack.school.notes.base.ServerErrorCode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLogin {
    String message() default "Incorrect login";

    ServerErrorCode errorCode() default ServerErrorCode.INVALID_LOGIN;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
