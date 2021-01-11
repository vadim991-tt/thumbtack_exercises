package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.base.ServerErrorCode;
import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.dto.request.author.ChangePasswordDtoRequest;
import net.thumbtack.school.notes.dto.request.author.RegisterAuthorDtoRequest;
import net.thumbtack.school.notes.dto.request.note.ChangeNoteDtoRequest;
import net.thumbtack.school.notes.dto.response.author.AuthorInfoDtoResponse;
import net.thumbtack.school.notes.dto.response.author.ChangePasswordDtoResponse;
import net.thumbtack.school.notes.dto.response.author.AuthorDtoResponse;
import net.thumbtack.school.notes.dto.response.author.RegisterAuthorDtoResponse;
import net.thumbtack.school.notes.mapstruct.DtoAuthorMapper;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Role;
import net.thumbtack.school.notes.view.AuthorView;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service("authorService")
public class AuthorService {
    private final AuthorDao authorDao;

    @Autowired
    public AuthorService(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    public String registerAuthor(RegisterAuthorDtoRequest request) throws ServerException {
        Author author = DtoAuthorMapper.INSTANCE.authorFromRegisterDto(request);
        return authorDao.registerAuthor(author);
    }

    public String loginAuthor(String login, String password) throws ServerException {
        return authorDao.login(login, password);
    }

    public void logout(String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        authorDao.logout(uuid);
    }

    public AuthorInfoDtoResponse getUserInfo(String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        return DtoAuthorMapper.INSTANCE.infoFromAuthor(author);
    }

    public void deleteAuthor(String uuid, String password) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        authorDao.deleteAuthor(author);
        authorDao.logout(uuid);
    }

    public ChangePasswordDtoResponse changeUserInfo(ChangePasswordDtoRequest req, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        Author authorToResp = authorDao.changeAuthorInformation(
                author.getId(),
                req.getFirstName(),
                req.getLastName(),
                req.getPatronymicName(),
                req.getNewPassword(),
                req.getOldPassword());
        return DtoAuthorMapper.INSTANCE.changePassDtoFromAuthor(authorToResp);

    }

    public void addSuperPrivileges(String uuid, int id) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        authorDao.addSuperPrivileges(author, id);
    }

    // Test it
    public List<? extends AuthorDtoResponse> getAuthors(String uuid, Map<String, String> params) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        params.put("id", String.valueOf(author.getId()));
        Role role = author.getRole();
        if (role.equals(Role.SUPER)) {
            params.put("superUserRequest", "true");
        } else if (params.get("type") != null && params.get("type").equals("super")) {
            params.remove("type"); // Ignored: "You are not super user"
        }
        List<AuthorView> authors = authorDao.getAllUsingSQLBuilder(params);
        if (role.equals(Role.SUPER)) {
            return DtoAuthorMapper.INSTANCE.authorsBySuperFromViews(authors);
        } else {
            return DtoAuthorMapper.INSTANCE.authorsFromViews(authors);
        }

    }

    public void followAuthor(String uuid, String login) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        authorDao.followAuthor(author, login);
    }

    public void ignoreAuthor(String uuid, String login) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        authorDao.ignoreUser(author, login);
    }

    public void unfollowAuthor(String uuid, String login) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        authorDao.unfollowAuthor(author, login);
    }

    public void stopIgnoringAuthor(String uuid, String login) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        authorDao.stopIgnoringAuthor(author, login);
    }


}