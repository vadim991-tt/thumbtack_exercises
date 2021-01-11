package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Comment;

import java.util.List;

public interface CommentDao {

    Comment insertComment(Author author, Comment comment, int noteId) throws ServerException;

    List<Comment> getComments(Author author, int noteId) throws ServerException;

    Comment changeBody(Author author, int id, String newBody) throws ServerException;

    void delete(Author author, int id) throws ServerException;

    void deleteFromNote(Author author, int noteId) throws ServerException;
}
