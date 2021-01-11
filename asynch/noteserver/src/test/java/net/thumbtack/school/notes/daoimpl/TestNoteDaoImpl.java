package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dao.CommentDao;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.dao.NoteDao;
import net.thumbtack.school.notes.dao.SectionDao;
import net.thumbtack.school.notes.utils.MyBatisUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

public class TestNoteDaoImpl {
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
    public void testInsert() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section section = new Section(0, "Section");
        sectionDao.insertSection(section, author);

        Note noteToDB = new Note("Subject", "Body");
        Note noteAfterInsertion = noteDao.insertNote(author, noteToDB, section.getId());
        Note noteFromDB = noteDao.getNoteById(author, noteAfterInsertion.getId());

        assertNotEquals(0, noteAfterInsertion.getId());
        assertEquals(noteFromDB.getId(), noteAfterInsertion.getId());
    }


    @Test
    public void getNoteInfo() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section section = new Section(0, "Section");
        sectionDao.insertSection(section, author);

        for (int i = 0; i < 10; i++) {
            Note note = new Note(String.format("Subject%s", i), "Body");
            noteDao.insertNote(author, note, section.getId());
        }
        Note noteToGet = new Note("NewSubject", "Body");
        noteDao.insertNote(author, noteToGet, section.getId());

        Note noteFromDB = noteDao.getNoteById(author, noteToGet.getId());
        assertEquals(noteFromDB.getSubject(), noteToGet.getSubject());
        assertEquals(noteFromDB.getBody(), noteFromDB.getBody());

    }

    @Test
    public void changeNoteInfo() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section section = new Section(0, "Section");
        sectionDao.insertSection(section, author);

        Note note = new Note("Subject", "Body");
        noteDao.insertNote(author, note, section.getId());

        Note noteFromDB = noteDao.getNoteById(author, note.getId());
        assertEquals(noteFromDB.getSubject(), note.getSubject());

        noteDao.changeBody(author, note.getId(), "newBody");
        noteFromDB = noteDao.getNoteById(author, note.getId());
        assertEquals(noteFromDB.getBody(), "newBody");

    }

    @Test
    public void moveNote() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);

        Section secondSection = new Section(0, "SecondSection");
        sectionDao.insertSection(secondSection, author);

        Note noteBeforeMoving = new Note("Subject", "Body");
        noteDao.insertNote(author, noteBeforeMoving, firstSection.getId());

        noteDao.move(author, noteBeforeMoving.getId(), secondSection.getId());
        Note noteAfterMoving = noteDao.getNoteById(author, noteBeforeMoving.getId());

        assertEquals(secondSection.getName(), noteAfterMoving.getSection().getName());
        assertEquals(secondSection.getId(), noteAfterMoving.getSection().getId());

    }

    @Test
    public void changeInfoAndMove() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);

        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);
        Section secondSection = new Section(0, "SecondSection");
        sectionDao.insertSection(secondSection, author);

        Note noteBefore = new Note("Subject", "Body");
        noteDao.insertNote(author, noteBefore, firstSection.getId());

        Note noteFromDbBefore = noteDao.getNoteById(author, noteBefore.getId());
        assertEquals(noteFromDbBefore.getSubject(), noteBefore.getSubject());
        assertEquals(noteFromDbBefore.getSection().getId(), firstSection.getId());

        noteDao.changeBodyAndMove(author, noteBefore.getId(), secondSection.getId(), "newBody");
        Note noteFromDBAfter = noteDao.getNoteById(author, noteBefore.getId());
        assertEquals("newBody", noteFromDBAfter.getBody());
        assertEquals(secondSection.getId(), noteFromDBAfter.getSection().getId());
    }

    @Test
    public void deleteNote() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);

        Note note = new Note("Subject", "Body");
        noteDao.insertNote(author, note, firstSection.getId());
        Note noteBefore = noteDao.getNoteById(author, note.getId());
        assertNotNull(noteBefore);

        noteDao.deleteNote(author, note.getId());
        Note noteFromDB = noteDao.getNoteById(author, note.getId());
        assertNull(noteFromDB);

    }

    @Test
    public void rateNote() throws ServerException {
        Random random = new Random();
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);

        Note firstNote = new Note("Subject1", "Body1");
        noteDao.insertNote(author, firstNote, firstSection.getId());

        Note secondNote = new Note("Subject2", "Body2");
        noteDao.insertNote(author, secondNote, firstSection.getId());

        for (int i = 1; i < 11; i++) {
            Author someAuthor = new Author("FirstName", "LastName", "Patronymic", String.format("Login%s", i), "password");
            authorDao.registerAuthor(someAuthor);
            noteDao.rateNote(someAuthor, firstNote.getId(), random.nextInt(6));
            noteDao.rateNote(someAuthor, secondNote.getId(), random.nextInt(6));
        }

        Note firstNoteFromDB = noteDao.getNoteById(author, firstNote.getId());
        Note secondNoteFromDB = noteDao.getNoteById(author, secondNote.getId());
        double firstRating = firstNoteFromDB.getRating();
        double secondRating = secondNoteFromDB.getRating();
        double notesAverage = (firstRating + secondRating) / 2;

        Author creator = authorDao.getAuthorByUUID(uuid);
        double rating = creator.getRating();

        assertEquals(notesAverage, rating);
    }

    @Test
    public void rateNote2() throws ServerException {
        Random random = new Random();
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);

        Note firstNote = new Note("Subject1", "Body1");
        noteDao.insertNote(author, firstNote, firstSection.getId());

        Note secondNote = new Note("Subject2", "Body2");
        noteDao.insertNote(author, secondNote, firstSection.getId());

        List<Author> authors = new ArrayList<>(10);
        for (int i = 1; i < 11; i++) {
            Author someAuthor = new Author("FirstName", "LastName", "Patronymic", String.format("Login%s", i), "password");
            authorDao.registerAuthor(someAuthor);
            authors.add(someAuthor);
            noteDao.rateNote(someAuthor, firstNote.getId(), random.nextInt(6));
            noteDao.rateNote(someAuthor, secondNote.getId(), random.nextInt(6));
        }

        Note firstNoteFromDB = noteDao.getNoteById(author, firstNote.getId());
        Note secondNoteFromDB = noteDao.getNoteById(author, secondNote.getId());

        double firstRating = firstNoteFromDB.getRating();
        double secondRating = secondNoteFromDB.getRating();
        double notesAverage = (firstRating + secondRating) / 2;

        double rating = authorDao.getAuthorByUUID(uuid).getRating();
        assertEquals(notesAverage, rating);

        // And now they changed their minds
        for (Author someAuthor : authors) {
            noteDao.rateNote(someAuthor, firstNote.getId(), random.nextInt(6));
            noteDao.rateNote(someAuthor, secondNote.getId(), random.nextInt(6));
        }

        Note firstNoteFromDB2 = noteDao.getNoteById(author, firstNote.getId());
        Note secondNoteFromDB2 = noteDao.getNoteById(author, secondNote.getId());

        double firstRating2 = firstNoteFromDB2.getRating();
        double secondRating2 = secondNoteFromDB2.getRating();
        double notesAverage2 = (firstRating2 + secondRating2) / 2;

        double rating2 = authorDao.getAuthorByUUID(uuid).getRating();
        assertNotEquals(rating, rating2);
        assertEquals(notesAverage2, rating2);
    }

    @Test
    public void testGetNotes() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);

        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);
        Note note = new Note("Subject", "Body");
        noteDao.insertNote(author, note, firstSection.getId());

        for (int i = 0; i < 5; i++) {
            Comment comment = new Comment();
            comment.setBody("Body");
            commentDao.insertComment(author, comment, note.getId());
        }

        for (int i = 0; i < 5; i++) {
            noteDao.changeBody(author, note.getId(), String.format("newBody%s", i));
            for (int j = 0; j < 5; j++) {
                Comment comment = new Comment();
                comment.setBody("newComment");
                commentDao.insertComment(author, comment, note.getId());
            }
        }

        List<Note> notes = noteDao.getAll(new HashMap<>());


    }

    @Test
    public void testGetNotesWithParams() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);

        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);
        Note note = new Note("Subject", "Body");
        noteDao.insertNote(author, note, firstSection.getId());

        Map<String, String> params = new HashMap<>();
        params.put("sectionId", String.valueOf(firstSection.getId()));
        params.put("sortByRating", "asc");
        params.put("user", String.valueOf(author.getId()));

        List<Note> notes = noteDao.getAll(params);

    }

    @Test
    public void testGetNotesWithAllTags() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        authorDao.registerAuthor(author);

        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);
        Note note = new Note("Subject", "Body cool nice omg");
        noteDao.insertNote(author, note, firstSection.getId());

        Map<String, String> params = new HashMap<>();
        params.put("allTags", "true");
        params.put("tags", "tagThatDoesNotExist, cool");
        params.put("sortByRating", "asc");

        List<Note> notes = noteDao.getAll(params);
        assertEquals(1, notes.size());
    }

    @Test
    public void testGetNotesWithTags() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        authorDao.registerAuthor(author);

        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);
        Note note = new Note("Subject", "Body cool nice omg");
        noteDao.insertNote(author, note, firstSection.getId());

        Map<String, String> params = new HashMap<>();
        params.put("tags", "tagThatDoesNotExist, cool");
        params.put("sortByRating", "asc");

        List<Note> notes = noteDao.getAll(params);
        assertEquals(0, notes.size());
    }

    @Test
    public void testGetNotesWithTags2() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        authorDao.registerAuthor(author);

        Section firstSection = new Section(0, "FirstSection");
        sectionDao.insertSection(firstSection, author);
        Note note = new Note("Subject", "Body cool nice omg");
        noteDao.insertNote(author, note, firstSection.getId());

        Map<String, String> params = new HashMap<>();
        params.put("tags", "nice,cool");
        params.put("sortByRating", "asc");

        List<Note> notes = noteDao.getAll(params);
        assertEquals(1, notes.size());
    }




}
