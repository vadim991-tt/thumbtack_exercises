package net.thumbtack.school.spring.model;

import java.time.ZonedDateTime;
import java.util.Objects;

public class DtoResponse {
    private String message;

    public DtoResponse() {

    }

    public DtoResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DtoResponse that = (DtoResponse) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
