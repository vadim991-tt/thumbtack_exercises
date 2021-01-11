package net.thumbtack.school.concert.dto.request.user;

import net.thumbtack.school.concert.base.user.UserErrorCode;
import net.thumbtack.school.concert.base.user.UserException;

import java.util.Objects;

public class LoginUserDtoRequest {
    private String login;
    private String password;

    public LoginUserDtoRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public static void validate(LoginUserDtoRequest request) throws UserException {
        if (request.getLogin() == null || request.getLogin().isBlank())
            throw new UserException(UserErrorCode.LOGIN_ERROR);
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new UserException(UserErrorCode.PASSWORD_ERROR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginUserDtoRequest that = (LoginUserDtoRequest) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
