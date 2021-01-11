package net.thumbtack.school.notes.handler;

import net.thumbtack.school.notes.base.ServerErrorCode;
import net.thumbtack.school.notes.base.ServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<CustomError> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Object[] args = error.getArguments();
            assert args != null;
            String errorCode = args[1].toString();
            String field = error.getField();
            errors.add(new CustomError(errorCode, field, error.getDefaultMessage()));
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            Object[] args = error.getArguments();
            assert args != null;
            String errorCode = args[1].toString();
            String field = args[2].toString();
            errors.add(new CustomError(errorCode, field, error.getDefaultMessage()));
        }

        ApiError apiError = new ApiError(errors);
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler({ServerException.class})
    public ResponseEntity<Object> handleServerException(ServerException ex, WebRequest request){
        CustomError error = new CustomError(ex.getErrorCode().toString(), ex.getField(), ex.getMessage());
        List<CustomError> errors = new ArrayList<>();
        errors.add(error);
        ApiError apiError = new ApiError(errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(RuntimeException ex){
        CustomError error = new CustomError(null, null, ex.getMessage());
        List<CustomError> errors = new ArrayList<>();
        errors.add(error);
        ApiError apiError = new ApiError(errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }





}
