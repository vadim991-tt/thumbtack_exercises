package net.thumbtack.school.concert.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Comment {
    private String author;
    private String text;
    private Set<Comment> comments;
    private String song;
    private String previousComment;

    public Comment(String author, String text, String song) {
        this.author = author;
        this.text = text;
        this.song = song;
        this.comments = new HashSet<>();
    }

    public Comment(String author, String text, String song, String previousComment) {
        this(author, text, song);
        this.previousComment = previousComment;
    }

    public String getAuthor() {
        return author;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public String getSong() {
        return song;
    }

    public String getText() {
        return text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPreviousComment() {
        return previousComment;
    }

    public void setPreviousComment(String previousComment) {
        this.previousComment = previousComment;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(author, comment.author) &&
                Objects.equals(text, comment.text) &&
                Objects.equals(comments, comment.comments) &&
                Objects.equals(song, comment.song) &&
                Objects.equals(previousComment, comment.previousComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, text, comments, song, previousComment);
    }
}
