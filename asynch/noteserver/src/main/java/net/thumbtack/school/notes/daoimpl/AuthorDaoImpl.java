package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.base.ServerErrorCode;
import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.mappers.AuthorMapper;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Role;
import net.thumbtack.school.notes.view.AuthorView;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Qualifier("authorDaoImpl")
public class AuthorDaoImpl extends DaoImplBase implements AuthorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorDaoImpl.class);

    @Value("${user_idle_timeout}")
    private int user_idle_timeout;


    @Override
    public String registerAuthor(Author author) throws ServerException {
        LOGGER.debug("DAO insert Author {}", author);
        String uuid = UUID.randomUUID().toString();
        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).insert(author);
                getAuthorMapper(sqlSession).loginById(author.getId(), uuid);
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert user {} {}", author, e);
                if (e.getMessage().contains("Duplicate entry")) {
                    throw new ServerException(ServerErrorCode.LOGIN_ALREADY_EXISTS, author.getLogin(), "login");
                }
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return uuid;
    }

    @Override
    public String login(String login, String password) throws ServerException {
        LOGGER.debug("DAO login author with {} login: ", login);
        String uuid = UUID.randomUUID().toString();
        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).login(login, password, uuid);
            } catch (RuntimeException e) {
                if (e.getMessage().contains("Column 'authorId' cannot be null")) {
                    throw new ServerException(ServerErrorCode.LOGIN_ERROR);
                }
                LOGGER.info("Can't login author with {} login : {}", login, e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return uuid;

    }

    @Override
    public String logout(String uuid) throws ServerException {
        LOGGER.debug("DAO logout author with java session id: {}", uuid);
        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).logout(uuid);
            } catch (RuntimeException e) {
                LOGGER.info("Can't logout author with java session id : {} {}", uuid, e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return uuid;
    }

    @Override
    public Author getAuthorByUUID(String uuid) throws ServerException {
        LOGGER.debug("DAO getAuthorByUUID: {}", uuid);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getAuthorMapper(sqlSession).getByUUID(uuid);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get user with uuid: {} {}", uuid, e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void validateToken(String uuid) throws ServerException {
        LOGGER.debug("DAO validate token: {}", uuid);
        try (SqlSession sqlSession = getSession()) {
            try {
                AuthorMapper authorMapper = getAuthorMapper(sqlSession);
                Timestamp timestamp = authorMapper.getSessionTime(uuid);
                if (timestamp != null) {
                    long creationTime = timestamp.getTime();
                    long currentTime = new Date().getTime();
                    if ((currentTime - creationTime) / 1000 > user_idle_timeout) {
                        authorMapper.logout(uuid);
                    }
                    authorMapper.updateSessionTime(uuid);
                } else {
                    throw new ServerException(ServerErrorCode.INVALID_TOKEN);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't validate token: {} {}", uuid, e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }


    @Override
    public void deleteAuthor(Author author) throws ServerException {
        LOGGER.debug("DAO delete author with id: {}", author.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).deleteAuthor(author.getPassword(), author.getId());
            } catch (RuntimeException e) {
                LOGGER.info("Can't delete author with id: {} {}", author.getId(), e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }


    @Override
    public Author changeAuthorInformation(int id, String firstName, String lastName, String patronymic, String oldPass, String newPass) throws ServerException {
        LOGGER.debug("DAO change author info with {} id", id);
        Author toReturn;
        try (SqlSession sqlSession = getSession()) {
            try {
                if (patronymic == null) {
                    getAuthorMapper(sqlSession).changeUserInfo(id, firstName, lastName, newPass, oldPass);
                } else {
                    getAuthorMapper(sqlSession).changeUserInfoWithPatronymic(id, firstName, lastName, patronymic, newPass, oldPass);
                }
                toReturn = getAuthorMapper(sqlSession).getById(id);
            } catch (RuntimeException e) {
                LOGGER.info("Can't change author info with {} id: {}", id, e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return toReturn;
    }

    @Override
    public void addSuperPrivileges(Author author, int id) throws ServerException {
        LOGGER.debug("DAO add super privileges to author with id: {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Role role = author.getRole();
                if (role == Role.SUPER) {
                    getAuthorMapper(sqlSession).addUserToSuper(id);
                } else {
                    throw new ServerException(ServerErrorCode.PERMISSION_ERROR);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't add super privileges to author with {} id: ", id, e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void followAuthor(Author author, String login) throws ServerException {
        LOGGER.debug("DAO add {} to follow list: [Requester id: {}]", login, author.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).stopIgnoringUser(author.getId(), login);
                getAuthorMapper(sqlSession).followUser(author.getId(), login);
            } catch (RuntimeException e) {
                sqlSession.rollback();
                LOGGER.info("Can't add {} to follow list: [Requester uuid: {}] {}", login, author.getId(), e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void ignoreUser(Author author, String login) throws ServerException {
        LOGGER.debug("DAO add {} to ignore list: [Requester id: {}]", login, author.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).unfollowUser(author.getId(), login);
                getAuthorMapper(sqlSession).ignoreUser(author.getId(), login);
            } catch (RuntimeException e) {
                sqlSession.rollback();
                LOGGER.info("Can't add {} to ignore list: [Requester id: {}] {}", login, author.getId(), e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void unfollowAuthor(Author author, String login) throws ServerException {
        LOGGER.debug("DAO unfollow {}: [Requester id: {}]", login, author.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).unfollowUser(author.getId(), login);
            } catch (RuntimeException e) {
                LOGGER.info("Can't unfollow {}: [Requester id: {}] {}", login, author.getId(), e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void stopIgnoringAuthor(Author author, String login) throws ServerException {
        LOGGER.debug("DAO stop ignoring author {}: [Requester id: {}]", login, author.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).stopIgnoringUser(author.getId(), login);
            } catch (RuntimeException e) {
                LOGGER.info("Unable to stop ignoring {}: [Requester id: {}]", login, author.getId(), e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<AuthorView> getAllUsingSQLBuilder(Map<String, String> map) throws ServerException {
        LOGGER.debug("DAO get authors using SQLBuilder {}", map);

        String id = null;
        String typeCondition = null;
        String countCondition = null;
        String fromCondition = null;
        String superUserRequest = null;
        String sortByRating = null;

        if (map != null) {
            id = map.get("id");
            typeCondition = map.get("type");
            countCondition = map.get("count");
            fromCondition = map.get("from");
            superUserRequest = map.get("superUserRequest");
            sortByRating = map.get("sortByRating");
        }

        try (SqlSession sqlSession = getSession()) {
            return getAuthorMapper(sqlSession).getUsingSQLBuilder(id, superUserRequest, sortByRating, typeCondition, countCondition, fromCondition);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get authors Using SQL Builder", ex);
            throw new ServerException(ServerErrorCode.DATABASE_ERROR);
        }
    }


    // Test methods
    @Override
    public void deleteAll() {
        LOGGER.debug("Deleting users and sessions");
        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).deleteAll();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            sqlSession.commit();
        }
    }

    @Override
    public String registerAdmin(Author admin) throws ServerException {
        LOGGER.debug("DAO insert Author {}", admin);
        String uuid = UUID.randomUUID().toString();

        try (SqlSession sqlSession = getSession()) {
            try {
                getAuthorMapper(sqlSession).insertAdmin(admin);
                getAuthorMapper(sqlSession).login(admin.getLogin(), admin.getPassword(), uuid);
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert user {} {}", admin, e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return uuid;
    }


}
