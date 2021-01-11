// REVU DAO - понятие более общее, чем MyBatis
// так что лучше net.thumbtack.school.notes.dao.mybatis;
// а еще лучше просто net.thumbtack.school.notes.dao
// при чем тут MyBatis ? Это же интерфейс, он от имплементации не должен зависеть
package net.thumbtack.school.notes.dao;


import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Role;
import net.thumbtack.school.notes.view.AuthorView;

import java.util.List;
import java.util.Map;

public interface AuthorDao {

    String registerAuthor(Author author) throws ServerException;

    Author getAuthorByUUID(String uuid) throws ServerException;

    String login(String login, String password) throws ServerException;

    String logout(String uuid) throws ServerException;

    void deleteAuthor(Author author) throws ServerException;

    void validateToken(String uuid) throws ServerException;

    Author changeAuthorInformation(int id, String firstName, String lastName, String patronymic, String oldPass, String newPass) throws ServerException;

    void addSuperPrivileges(Author author, int id) throws ServerException;

    void followAuthor(Author author, String login) throws ServerException;

    void ignoreUser(Author author, String login) throws ServerException;

    void unfollowAuthor(Author author, String login) throws ServerException;

    void stopIgnoringAuthor(Author author, String login) throws ServerException;

    List<AuthorView> getAllUsingSQLBuilder(Map<String, String> map) throws ServerException;

    // Test methods
    void deleteAll();

    String registerAdmin(Author author) throws ServerException;
}
