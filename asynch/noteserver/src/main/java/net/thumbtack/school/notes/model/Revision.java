package net.thumbtack.school.notes.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Revision {
    private int id;
    private String body;
    private Timestamp created;
    private List<Comment> comments;

    public Revision(){

    }

    public Revision(int id, String body, Timestamp created, List<Comment> comments) {
        this.id = id;
        this.body = body;
        this.created = created;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Revision revision = (Revision) o;
        return id == revision.id &&
                Objects.equals(body, revision.body) &&
                Objects.equals(created, revision.created) &&
                Objects.equals(comments, revision.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, body, created, comments);
    }
}
