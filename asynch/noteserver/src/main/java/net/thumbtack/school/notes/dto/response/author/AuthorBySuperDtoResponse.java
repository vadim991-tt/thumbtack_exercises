package net.thumbtack.school.notes.dto.response.author;

import java.util.Objects;


public class AuthorBySuperDtoResponse extends AuthorDtoResponse {
    private boolean isSuper;

    public AuthorBySuperDtoResponse(int id, String firstName, String lastName, String patronymic, String login, String timeRegistered, boolean isOnline, boolean isDeleted, int rating, boolean isSuper) {
        super(id, firstName, lastName, patronymic, login, timeRegistered, isOnline, isDeleted, rating);
        this.isSuper = isSuper;
    }

    public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorBySuperDtoResponse that = (AuthorBySuperDtoResponse) o;
        return isSuper == that.isSuper;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isSuper);
    }
}
