package net.thumbtack.school.notes.validator.author;

import net.thumbtack.school.notes.base.ServerErrorCode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PatronymicValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPatronymic {
    String message() default "Invalid patronymic";

    ServerErrorCode errorCode() default ServerErrorCode.INVALID_PATRONYMIC;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
