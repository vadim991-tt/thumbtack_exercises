package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Note;

import java.util.List;
import java.util.Map;

public interface NoteDao {

    Note insertNote(Author author, Note note,  int SectionId) throws ServerException;

    Note getNoteInfo(Author author, int id) throws ServerException;

    Note getNoteById(Author author, int id) throws ServerException;

    Note changeBody(Author author, int id, String body) throws ServerException;

    Note move(Author author, int id, int sectionId) throws ServerException;

    Note changeBodyAndMove(Author author, int id, int sectionId, String body) throws ServerException;

    void deleteNote(Author author, int id) throws ServerException;

    void rateNote(Author author, int id, int rating) throws ServerException;

    List<Note> getAll(Map<String, String> map) throws ServerException;
}
