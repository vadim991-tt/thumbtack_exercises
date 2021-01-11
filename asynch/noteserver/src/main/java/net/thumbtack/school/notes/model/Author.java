package net.thumbtack.school.notes.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Author {

    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
    private double rating;
    private int createdNotes;
    private Timestamp timeRegistered;
    private Role role;
    private boolean isDeleted;
    private List<Note> notes;
    private List<Section> sections;

    public Author() {

    }

    public Author(int id, String firstName, String lastName, String patronymic, String login, String password, double rating,
                  int createdNotes, Timestamp timeRegistered, Role role, boolean isDeleted, List<Note> notes, List<Section> sections) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
        this.rating = rating;
        this.createdNotes = createdNotes;
        this.timeRegistered = timeRegistered;
        this.role = role;
        this.isDeleted = isDeleted;
        this.notes = notes;
        this.sections = sections;
    }

    public Author(String firstName, String lastName, String patronymic, String login, String password, double rating,
                  int createdNotes, Timestamp timeRegistered, Role role, boolean isDeleted, List<Note> notes, List<Section> sections) {
        this(0, firstName, lastName, patronymic, login, password, rating, createdNotes, timeRegistered, role, isDeleted, notes, sections);
    }

    public Author(String firstName, String lastName, String patronymic, String login, String password) {
        this(0, firstName, lastName, patronymic, login, password, 0, 0, null, Role.REGULAR,
                false, new ArrayList<>(), new ArrayList<>());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCreatedNotes() {
        return createdNotes;
    }

    public void setCreatedNotes(int createdNotes) {
        this.createdNotes = createdNotes;
    }

    public Timestamp getTimeRegistered() {
        return timeRegistered;
    }

    public void setTimeRegistered(Timestamp timeRegistered) {
        this.timeRegistered = timeRegistered;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


    // Authors are equal where their main fields are equal. Exceptions: TimeRegistered, notes, sections.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author1 = (Author) o;
        return id == author1.id &&
                Double.compare(author1.rating, rating) == 0 &&
                createdNotes == author1.createdNotes &&
                isDeleted == author1.isDeleted &&
                Objects.equals(firstName, author1.firstName) &&
                Objects.equals(lastName, author1.lastName) &&
                Objects.equals(patronymic, author1.patronymic) &&
                Objects.equals(login, author1.login) &&
                Objects.equals(password, author1.password) &&
                role == author1.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, login, password, rating, createdNotes, role, isDeleted);
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Author author = (Author) o;
//        return id == author.id &&
//                Double.compare(author.rating, rating) == 0 &&
//                createdNotes == author.createdNotes &&
//                isDeleted == author.isDeleted &&
//                Objects.equals(firstName, author.firstName) &&
//                Objects.equals(lastName, author.lastName) &&
//                Objects.equals(patronymic, author.patronymic) &&
//                Objects.equals(login, author.login) &&
//                Objects.equals(password, author.password) &&
//                Objects.equals(timeRegistered, author.timeRegistered) &&
//                role == author.role &&
//                Objects.equals(notes, author.notes) &&
//                Objects.equals(sections, author.sections);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, firstName, lastName, patronymic, login, password, rating, createdNotes, timeRegistered, role, isDeleted, notes, sections);
//    }
}
