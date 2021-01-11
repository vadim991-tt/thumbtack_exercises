package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Role;
import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.dao.SectionDao;
import net.thumbtack.school.notes.utils.MyBatisUtils;
import net.thumbtack.school.notes.view.AuthorView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestAuthorDaoImpl {

    private final AuthorDao authorDao = new AuthorDaoImpl();
    private final SectionDao sectionDao = new SectionDaoImpl();

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
    public void testRegisterAuthor() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Author authorFromDB = authorDao.getAuthorByUUID(uuid);


        assertAll(
                () -> assertEquals(author.getId(), authorFromDB.getId()),
                () -> assertEquals(author.getFirstName(), authorFromDB.getFirstName()),
                () -> assertEquals(author.getLastName(), authorFromDB.getLastName()),
                () -> assertEquals(author.getPatronymic(), authorFromDB.getPatronymic()),
                () -> assertEquals(author.getLogin(), authorFromDB.getLogin()),
                () -> assertEquals(author.getPassword(), authorFromDB.getPassword())
        );
    }

    @Test
    public void testRegisterAuthorError() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        authorDao.registerAuthor(author);
        assertThrows(ServerException.class, () -> authorDao.registerAuthor(author));
    }



    @Test
    public void testLoginLogout() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        authorDao.logout(uuid);
        String uuid2 = authorDao.login(author.getLogin(), author.getPassword());
        Author authorFromDB3 = authorDao.getAuthorByUUID(uuid2);
        assertEquals(author.getId(), authorFromDB3.getId());
    }


    @Test
    public void testGetAuthorInfo() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);
        Author authorFromDB = authorDao.getAuthorByUUID(uuid);
        assertEquals(author.getLogin(), authorFromDB.getLogin());
    }

    @Test
    public void testDeleteAuthor() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        String uuid = authorDao.registerAuthor(author);

        authorDao.deleteAuthor(author);
        authorDao.logout(uuid);
        Author authorFromDB = authorDao.getAuthorByUUID(uuid);
        assertNull(authorFromDB);
    }


    @Test
    public void testLoginIntoDeletedAccount() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "password");
        authorDao.registerAuthor(author);
        authorDao.deleteAuthor(author);

        try {
            authorDao.login(author.getLogin(), author.getPassword());
            fail();
        } catch (ServerException exception) {
            // ignored
        }
    }

    @Test
    public void testChangeUserInfo() throws ServerException {
        Author author = new Author("FirstName", "LastName", "Patronymic", "Login", "oldPassword");
        String uuid = authorDao.registerAuthor(author);
        Author authorFromDB = authorDao.getAuthorByUUID(uuid);

        assertEquals(author.getLogin(), authorFromDB.getLogin()); // to all fields

        authorDao.changeAuthorInformation(authorFromDB.getId(), "newFirstName", "newLastName", "newPatronymic", "oldPassword", "newPassword");
        Author authorFromDB2 = authorDao.getAuthorByUUID(uuid);

        assertAll(
                () -> assertNotEquals(authorFromDB, authorFromDB2),
                () -> assertEquals(authorFromDB2.getFirstName(), "newFirstName"),
                () -> assertEquals(authorFromDB2.getLastName(), "newLastName"),
                () -> assertEquals(authorFromDB2.getPatronymic(), "newPatronymic"),
                () -> assertEquals(authorFromDB2.getPassword(), "newPassword")
        );
    }


    @Test
    public void changeUserRole() throws ServerException {
        Author admin = new Author("Admin", "LastName", "Patronymic", "Admin", "Admin");
        admin.setRole(Role.SUPER);
        String adminUUID = authorDao.registerAdmin(admin);

        Author user = new Author("FirstName", "LastName", "Patronymic", "Login", "Password");
        String userUUID = authorDao.registerAuthor(user);

        Author userFromDB = authorDao.getAuthorByUUID(userUUID);
        assertEquals(userFromDB.getRole(), Role.REGULAR);

        authorDao.addSuperPrivileges(admin, userFromDB.getId());
        Author newAdmin = authorDao.getAuthorByUUID(userUUID);
        assertEquals(newAdmin.getRole(), Role.SUPER);
    }

    @Test
    public void changeUserRoleError() throws ServerException {
        Author regularUser = new Author("FirstName", "LastName", "Patronymic", "Login1", "Password");
        String regularUserUUID = authorDao.registerAuthor(regularUser);
        Author user = new Author("FirstName", "LastName", "Patronymic", "Login2", "Password");
        String userUUID = authorDao.registerAuthor(user);

        Author userFromDB = authorDao.getAuthorByUUID(userUUID);
        assertEquals(userFromDB.getRole(), Role.REGULAR);

        try {
            authorDao.addSuperPrivileges(regularUser, userFromDB.getId());
            fail();
        } catch (ServerException exception) {
            // ignored
        }
    }

    @Test
    public void getAuthorView() throws ServerException {
        for (int i = 0; i < 100; i++) {
            Author author = new Author("Firstname", "Lastname", "Patronymic", String.format("Login%s", i), "password");
            authorDao.registerAuthor(author);
        }

        Author admin = new Author("Firstname", "Lastname", "Patronymic", "admin", "password");
        admin.setRole(Role.SUPER);
        String uuid = authorDao.registerAuthor(admin);

        Map<String, String> conditions = new HashMap<>();
        conditions.put("uuid", uuid);
        conditions.put("superUserRequest", "true");
        conditions.put("type", "super");
        conditions.put("count", "10");
        conditions.put("from", "0");

        List<AuthorView> list = authorDao.getAllUsingSQLBuilder(conditions);
    }

    @Test
    public void getAuthorView2() throws ServerException {
        for (int i = 0; i < 10; i++) {
            Author author = new Author("Firstname", "Lastname", "Patronymic", String.format("Login%s", i), "password");
            authorDao.registerAuthor(author);
        }

        Author admin = new Author("Firstname", "Lastname", "Patronymic", "admin", "password");
        admin.setRole(Role.SUPER);
        String uuid = authorDao.registerAuthor(admin);

        Map<String, String> conditions = new HashMap<>();
        conditions.put("uuid", uuid);
        conditions.put("superUserRequest", "true");
        conditions.put("sortByRating", "asc");
        conditions.put("type", "super");
        conditions.put("count", "10");
        conditions.put("from", "0");

        List<AuthorView> list = authorDao.getAllUsingSQLBuilder(conditions);
    }

    @Test
    public void followAuthor() throws ServerException {

        for (int i = 0; i < 10; i++) {
            Author author = new Author("Firstname", "Lastname", "Patronymic", String.format("Login%s", i), "password");
            authorDao.registerAuthor(author);
        }

        Author authorToFollow = new Author("Firstname", "Lastname", "Patronymic", "authorToFollow", "password");
        authorDao.registerAuthor(authorToFollow);

        Author followedAuthor = new Author("Firstname", "Lastname", "Patronymic", "followedAuthor", "password");
        authorDao.registerAuthor(followedAuthor);
        authorDao.followAuthor(followedAuthor, authorToFollow.getLogin());

        Map<String, String> conditions = new HashMap<>();
        conditions.put("id", String.valueOf(followedAuthor.getId()));
        conditions.put("type", "following");

        List<AuthorView> followList = authorDao.getAllUsingSQLBuilder(conditions);
        assertEquals(followList.get(0).getLogin(), authorToFollow.getLogin());
    }

    @Test
    public void ignoreAuthor() throws ServerException {
        for (int i = 0; i < 10; i++) {
            Author author = new Author("Firstname", "Lastname", "Patronymic", String.format("Login%s", i), "password");
            authorDao.registerAuthor(author);
        }

        Author ignoredAuthor = new Author("Firstname", "Lastname", "Patronymic", "ignoredAuthor", "password");
        authorDao.registerAuthor(ignoredAuthor);

        Author unhappySubscriber = new Author("Firstname", "Lastname", "Patronymic", "unhappySubscriber", "password");
        authorDao.registerAuthor(unhappySubscriber);
        authorDao.ignoreUser(unhappySubscriber, ignoredAuthor.getLogin());

        Map<String, String> conditions = new HashMap<>();
        conditions.put("id", String.valueOf(unhappySubscriber.getId()));
        conditions.put("type", "ignore");

        List<AuthorView> ignoredList = authorDao.getAllUsingSQLBuilder(conditions);
        assertEquals(ignoredList.get(0).getLogin(), ignoredAuthor.getLogin());
    }

    @Test
    public void unfollowAuthor() throws ServerException {
        Author authorToFollow = new Author("Firstname", "Lastname", "Patronymic", "authorToFollow", "password");
        authorDao.registerAuthor(authorToFollow);
        Author followedAuthor = new Author("Firstname", "Lastname", "Patronymic", "followedAuthor", "password");
        authorDao.registerAuthor(followedAuthor);
        authorDao.followAuthor(followedAuthor, authorToFollow.getLogin());

        Map<String, String> conditions = new HashMap<>();
        conditions.put("id", String.valueOf(followedAuthor.getId()));
        conditions.put("type", "following");

        List<AuthorView> followListBefore = authorDao.getAllUsingSQLBuilder(conditions);
        assertEquals(followListBefore.get(0).getLogin(), authorToFollow.getLogin());

        authorDao.unfollowAuthor(followedAuthor, authorToFollow.getLogin());
        List<AuthorView> followListAfter = authorDao.getAllUsingSQLBuilder(conditions);
        assertTrue(followListAfter.isEmpty());
    }

    @Test
    public void stopIgnoringUser() throws ServerException {
        Author ignoredAuthor = new Author("Firstname", "Lastname", "Patronymic", "ignoredAuthor", "password");
        authorDao.registerAuthor(ignoredAuthor);

        Author unhappySubscriber = new Author("Firstname", "Lastname", "Patronymic", "unhappySubscriber", "password");
        authorDao.registerAuthor(unhappySubscriber);
        authorDao.ignoreUser(unhappySubscriber, ignoredAuthor.getLogin());

        Map<String, String> conditions = new HashMap<>();
        conditions.put("id", String.valueOf(unhappySubscriber.getId()));
        conditions.put("type", "ignore");

        List<AuthorView> ignoredListBefore = authorDao.getAllUsingSQLBuilder(conditions);
        assertEquals(ignoredListBefore.get(0).getLogin(), ignoredAuthor.getLogin());

        authorDao.stopIgnoringAuthor(unhappySubscriber, ignoredAuthor.getLogin());
        List<AuthorView> ignoredListAfter = authorDao.getAllUsingSQLBuilder(conditions);
        assertTrue(ignoredListAfter.isEmpty());
    }


}
