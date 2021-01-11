package net.thumbtack.school.notes.view;

import net.thumbtack.school.notes.model.Role;

import java.sql.Timestamp;
import java.util.Objects;

public class AuthorView {

    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private Timestamp timeRegistered;
    private String uuid;
    private boolean isDeleted;
    private Role role;
    private double rating;

    public AuthorView() {

    }

    public AuthorView(int id, String firstName, String lastName, String patronymic, String login, Timestamp timeRegistered, String uuid, boolean isDeleted, Role role, double rating) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.timeRegistered = timeRegistered;
        this.uuid = uuid;
        this.isDeleted = isDeleted;
        this.role = role;
        this.rating = rating;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Timestamp getTimeRegistered() {
        return timeRegistered;
    }

    public void setTimeRegistered(Timestamp timeRegistered) {
        this.timeRegistered = timeRegistered;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorView that = (AuthorView) o;
        return id == that.id &&
                Double.compare(that.rating, rating) == 0 &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(login, that.login) &&
                Objects.equals(timeRegistered, that.timeRegistered) &&
                Objects.equals(uuid, that.uuid) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, login, timeRegistered, uuid, role, rating);
    }
}
