package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.base.ServerErrorCode;
import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.mappers.NoteMapper;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Role;
import net.thumbtack.school.notes.dao.NoteDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Qualifier("noteDaoImpl")
public class NoteDaoImpl extends DaoImplBase implements NoteDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorDaoImpl.class);

    @Override
    public Note insertNote(Author author, Note note, int sectionId) throws ServerException {
        LOGGER.debug("DAO insert note with {} name.", note.getSubject());
        try (SqlSession sqlSession = getSession()) {
            try {
                getNoteMapper(sqlSession).insert(note, sectionId, author.getId());
                getNoteMapper(sqlSession).insertRevision(note.getId(), note.getBody());
                getAuthorMapper(sqlSession).increaseAuthorNotesQuantity(author.getId());
                note = getNoteMapper(sqlSession).getLatestVersion(note.getId());
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert note {} {}", note.getSubject(), e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return note;
    }

    @Override
    public Note getNoteInfo(Author author, int id) throws ServerException {
        LOGGER.debug("DAO get note information with {} id", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getNoteMapper(sqlSession).getLatestVersion(id);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get note information: ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public Note getNoteById(Author author, int id) throws ServerException {
        LOGGER.debug("DAO get note with {} id", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getNoteMapper(sqlSession).getNoteByID(id);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get note: ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
        }
    }


    @Override
    public Note changeBody(Author author, int id, String newBody) throws ServerException {
        LOGGER.debug("DAO change note body id: {}, newBody: {} ", id, newBody);
        Note noteToReturn;
        try (SqlSession sqlSession = getSession()) {
            try {
                getNoteMapper(sqlSession).changeBody(id, newBody, author.getId());
                noteToReturn = getNoteMapper(sqlSession).getLatestVersion(id);
                getNoteMapper(sqlSession).insertRevision(noteToReturn.getId(), noteToReturn.getBody());
            } catch (RuntimeException e) {
                LOGGER.info("Can't note information: ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return noteToReturn;
    }

    @Override
    public Note move(Author author, int id, int sectionId) throws ServerException {
        LOGGER.debug("DAO move note with {} id to {} section id", id, sectionId);
        Note noteToReturn;
        try (SqlSession sqlSession = getSession()) {
            try {
                NoteMapper noteMapper = getNoteMapper(sqlSession);
                if (author.getRole() == Role.SUPER) {
                    noteMapper.moveBySuper(id, sectionId);
                } else {
                    noteMapper.move(id, sectionId, author.getId());
                }
                noteToReturn = noteMapper.getLatestVersion(id);
            } catch (RuntimeException e) {
                LOGGER.info("Can't note information: ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return noteToReturn;
    }

    @Override
    public Note changeBodyAndMove(Author author, int id, int sectionId, String newBody) throws ServerException {
        LOGGER.debug("DAO move note with {} id to {} section id", id, sectionId);
        Note noteToReturn;
        try (SqlSession sqlSession = getSession()) {
            try {
                NoteMapper noteMapper = getNoteMapper(sqlSession);
                noteMapper.moveAndChangeBody(id, sectionId, newBody, author.getId());
                noteToReturn = noteMapper.getLatestVersion(id);
                noteMapper.insertRevision(noteToReturn.getId(), noteToReturn.getBody());
            } catch (RuntimeException e) {
                LOGGER.info("Can't obtain note information: ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return noteToReturn;
    }

    @Override
    public void deleteNote(Author author, int id) throws ServerException {
        LOGGER.debug("DAO delete note with {} id", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                if (author.getRole() == Role.SUPER) {
                    getNoteMapper(sqlSession).deleteBySuper(id);
                } else {
                    getNoteMapper(sqlSession).delete(id, author.getId());
                }
                getAuthorMapper(sqlSession).reduceAuthorNotesQuantity(author.getId());
            } catch (RuntimeException e) {
                LOGGER.info("Can't delete: ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }


    @Override
    public void rateNote(Author author, int noteId, int rating) throws ServerException {
        LOGGER.debug("DAO rate song with {} noteId", noteId);
        try (SqlSession sqlSession = getSession()) {
            try {
                Integer previousRating = getNoteMapper(sqlSession).getPreviousRating(noteId, author.getId());
                getNoteMapper(sqlSession).rateNote(noteId, author.getId(), rating);
                if (previousRating == null) {
                    getNoteMapper(sqlSession).countNoteRating(noteId, rating);
                } else {
                    getNoteMapper(sqlSession).changeNoteRating(noteId, rating, previousRating);
                }
                getAuthorMapper(sqlSession).countAuthorRating(noteId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't rate note with {} id: ", noteId, e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Note> getAll(Map<String, String> map) throws ServerException {
        LOGGER.debug("DAO get notes all using SQLBuilder {}", map);

        String id = null;
        String sectionId = null;
        String sortByRating = null;
        String tags = null;
        String allTags = null;
        String timeFrom = null;
        String timeTo = null;
        String user = null;
        String include = null;
        String commentVersion = null;
        String from = null;
        String count = null;

        if (map != null) {
            id = map.get("id");
            sectionId = map.get("sectionId");
            sortByRating = map.get("sortByRating");
            tags = map.get("tags");
            allTags = map.get("allTags");
            timeFrom = map.get("timeFrom");
            timeTo = map.get("timeTo");
            user = map.get("user");
            include = map.get("include");
            commentVersion = map.get("commentVersion");
            from = map.get("from");
            count = map.get("count");
        }

        try (SqlSession sqlSession = getSession()) {
            return getNoteMapper(sqlSession).getUsingSQLBuilder(id, sectionId, sortByRating, tags, allTags, timeFrom, timeTo,
                    user, include, commentVersion, from, count);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't insert get All Using SQL Builder", ex);
            throw new ServerException(ServerErrorCode.DATABASE_ERROR);
        }
    }
}