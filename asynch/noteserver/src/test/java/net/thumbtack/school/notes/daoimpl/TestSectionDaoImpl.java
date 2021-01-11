package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.dao.NoteDao;
import net.thumbtack.school.notes.dao.SectionDao;
import net.thumbtack.school.notes.utils.MyBatisUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TestSectionDaoImpl {

    protected AuthorDao authorDao = new AuthorDaoImpl();
    protected NoteDao noteDao = new NoteDaoImpl();
    protected SectionDao sectionDao = new SectionDaoImpl();

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
    public void insertSection() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        authorDao.registerAuthor(author);

        Section section = new Section(0, "Section");
        sectionDao.insertSection(section, author);
        assertNotEquals(0, section.getId());
    }

    @Test
    public void renameSection() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        authorDao.registerAuthor(author);

        Section section = new Section(0, "OldName");
        sectionDao.insertSection(section, author);
        Section sectionFromBefore = sectionDao.getSectionInfo(author, section.getId());

        sectionDao.renameSection(author, "newName", section.getId());
        Section sectionFromAfter = sectionDao.getSectionInfo(author, section.getId());
        assertNotEquals(sectionFromBefore, sectionFromAfter);

    }

    @Test
    public void deleteSection() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        authorDao.registerAuthor(author);

        List<Integer> sectionIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Section section = new Section(0, String.format("Name %s", i));
            sectionDao.insertSection(section, author);
            sectionIds.add(section.getId());
        }
        List<Section> sectionsBefore = sectionDao.getSections(author);
        assertEquals(10, sectionsBefore.size());

        for (Integer id : sectionIds) {
            sectionDao.deleteSection(author, id);
        }

        List<Section> sectionsAfter = sectionDao.getSections(author);
        assertEquals(0, sectionsAfter.size());
    }

    @Test
    public void getAll() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        authorDao.registerAuthor(author);

        int random = new Random().nextInt(100);

        for (int i = 0; i < random; i++) {
            Section section = new Section(0, String.format("Name %s", i));
            sectionDao.insertSection(section, author);
        }
        List<Section> list = sectionDao.getSections(author);
        assertEquals(list.size(), random);
    }


}
