package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.model.*;
import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.dao.CommentDao;
import net.thumbtack.school.notes.dao.NoteDao;
import net.thumbtack.school.notes.dao.SectionDao;
import net.thumbtack.school.notes.utils.MyBatisUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestCommentDaoImpl {
    private final AuthorDao authorDao = new AuthorDaoImpl();
    private final NoteDao noteDao = new NoteDaoImpl();
    private final SectionDao sectionDao = new SectionDaoImpl();
    private final CommentDao commentDao = new CommentDaoImpl();

    private static boolean setUpIsDone = false;

    @BeforeAll()
    public static void setUp() {
        if (!setUpIsDone) {
            boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();
            if (!initSqlSessionFactory) {
                throw new RuntimeException("Can't create connection, stop");
            }
            setUpIsDone = true;
        }
    }

    @BeforeEach()
    public void clearDatabase() {
        sectionDao.deleteAll();
        authorDao.deleteAll();
    }

    @Test
    public void insertComment() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);
        Note note = new Note("Subject", "Body");
        noteDao.insertNote(author, note, firstSection.getId());

        Comment comment = new Comment();
        comment.setBody("Body");
        Comment commentFromDB = commentDao.insertComment(author, comment, note.getId());
        assertNotEquals(0, commentFromDB.getId());
    }

    @Test
    public void getComments() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);
        Note note = new Note("Subject", "Body");
        noteDao.insertNote(author, note, firstSection.getId());
        List<Comment> commentsBefore = commentDao.getComments(author, note.getId());
        assertEquals(0, commentsBefore.size());

        for (int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setBody("Body");
            commentDao.insertComment(author, comment, note.getId());
        }

        List<Comment> commentsList = commentDao.getComments(author, note.getId());
        assertEquals(10, commentsList.size());

    }

    @Test
    public void renameComments() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);
        Note note = new Note("Subject", "Body");
        noteDao.insertNote(author, note, firstSection.getId());

        Comment comment = new Comment();
        comment.setBody("Body");
        Comment commentFromDB = commentDao.insertComment(author, comment,  note.getId());
        assertEquals("Body", commentFromDB.getBody());

    }

//    @Test
//    public void deleteComment() throws ServerException {
//        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
//        String uuid = authorDao.registerAuthor(author);
//        Section firstSection = new Section(0, "FirstSection");
//        sectionDao.insertSection(firstSection, uuid);
//        Note note = new Note("Subject", "Body");
//        noteDao.insertNote(note, firstSection.getId(), uuid);
//
//        Comment comment = new Comment();
//        comment.setBody("Body");
//        Comment commentFromDB = commentDao.insertComment(comment, uuid, note.getId());
//
//        List<Comment> commentsList = commentDao.getComments(note.getId());
//        assertEquals(1, commentsList.size());
//
//        commentDao.delete(comment.getId(), uuid);
//        commentsList = commentDao.getComments(note.getId());
//        assertEquals(0, commentsList.size());
//    }

    @Test
    public void deleteCommentsFromNote() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section section = new Section(0, "FirstSection");
        sectionDao.insertSection(section, author);
        Note note = new Note("Subject", "Body");
        noteDao.insertNote(author,  note, section.getId());

        for (int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setBody("Body");
            commentDao.insertComment(author, comment, note.getId());
        }

        List<Comment> comments1 = commentDao.getComments(author, note.getId());
        assertEquals(10, comments1.size());

        commentDao.deleteFromNote(author, note.getId());
        List<Comment> comments2 = commentDao.getComments(author, note.getId());
        assertEquals(0, comments2.size());
    }


}
