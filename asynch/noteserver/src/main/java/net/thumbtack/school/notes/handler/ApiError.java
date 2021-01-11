package net.thumbtack.school.notes.handler;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiError {
    private List<CustomError> errors;

    public ApiError(List<CustomError> errors) {
        this.errors = errors;
    }

    public List<CustomError> getErrors() {
        return errors;
    }

    public void setErrors(List<CustomError> errors) {
        this.errors = errors;
    }
}
