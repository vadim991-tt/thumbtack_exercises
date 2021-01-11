package net.thumbtack.school.notes.model;

import java.sql.Timestamp;

public class Comment {
    private int id;
    private String body;
    private Author author;
    private int noteId;
    private int revisionId; // Версия заметки, к которой было создано примечание
    private Timestamp created;

    public Comment() {

    }

    public Comment(int id, String body, Author author, int noteId, int revisionId, Timestamp created) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.revisionId = revisionId;
        this.created = created;
        this.noteId = noteId;
    }

    public Comment(String body, Author author, int noteId, int revisionId, Timestamp created) {
        this(0, body, author, noteId, revisionId, created);
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

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
