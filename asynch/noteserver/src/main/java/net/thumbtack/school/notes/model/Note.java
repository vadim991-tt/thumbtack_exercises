package net.thumbtack.school.notes.model;

import javax.security.auth.Subject;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Note {
    private int id;
    private String subject;
    private String body;
    private double rating;
    private Section section;
    private Author author;
    private Timestamp created;
    private List<Revision> revisions;

    public Note() {

    }

    public Note(String subject, String body){
        this.id = 0;
        this.subject = subject;
        this.body = body;
    }


    public Note(int id, String subject, String body, double rating, Section section, Author author, Timestamp created, List<Revision> revisions) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.rating = rating;
        this.section = section;
        this.author = author;
        this.created = created;
        this.revisions = revisions;
    }

    public Note(String subject, String body, double rating, Section section, Author author, Timestamp created, List<Revision> revisions) {
        this(0, subject, body, rating, section, author, created, revisions);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<Revision> getRevisions() {
        return revisions;
    }

    public void setRevisions(List<Revision> revisions) {
        this.revisions = revisions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                Double.compare(note.rating, rating) == 0 &&
                Objects.equals(subject, note.subject) &&
                Objects.equals(body, note.body) &&
                Objects.equals(section, note.section) &&
                Objects.equals(author, note.author) &&
                Objects.equals(created, note.created) &&
                Objects.equals(revisions, note.revisions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, body, rating, section, author, created, revisions);
    }
}
