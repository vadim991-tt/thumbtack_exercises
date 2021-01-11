package net.thumbtack.school.notes.validator.note;

import net.thumbtack.school.notes.base.ServerErrorCode;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ChangeNoteValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidChangeNoteRequest {

    ServerErrorCode errorCode() default ServerErrorCode.INVALID_CHANGE_NOTE_REQUEST;

    String message() default "Request contains invalid fields";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
