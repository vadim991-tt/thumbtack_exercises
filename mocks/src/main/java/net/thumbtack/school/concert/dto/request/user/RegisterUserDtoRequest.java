package net.thumbtack.school.concert.dto.request.user;

import net.thumbtack.school.concert.base.user.UserErrorCode;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.model.User;


public class RegisterUserDtoRequest {
    private String firstName;
    private String lastName;
    private String login;
    private String password;

    public RegisterUserDtoRequest(String firstName, String lastName, String login, String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setLogin(login);
        setPassword(password);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getLastName() {
        return lastName;
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

    public static void validate(RegisterUserDtoRequest registerUserDtoRequest) throws UserException {
        String firstName = registerUserDtoRequest.getFirstName();
        String lastName = registerUserDtoRequest.getLastName();
        String login = registerUserDtoRequest.getLogin();
        String password = registerUserDtoRequest.getPassword();
        if (firstName == null || firstName.isBlank()) {
            throw new UserException(UserErrorCode.USER_WRONG_FIRSTNAME, firstName);
        }
        if (lastName == null || lastName.isBlank()) {
            throw new UserException(UserErrorCode.USER_WRONG_LASTNAME, lastName);
        }
        if (login == null || !(login.matches("[0-9a-zA-Z]+")) || login.length() < 8) {
            throw new UserException(UserErrorCode.USER_WRONG_LOGIN, login);
        }
        if (password == null || !(password.matches("[0-9a-zA-Z]+")) || password.length() < 8) {
            throw new UserException(UserErrorCode.USER_WRONG_PASSWORD);
        }
        if (password.toLowerCase().equals(password)) {
            throw new UserException(UserErrorCode.USER_WRONG_PASSWORD);
        }
    }

    public static User createUserFromDtoRequest(RegisterUserDtoRequest registerUserDtoRequest) {
        String firstName = registerUserDtoRequest.getFirstName();
        String lastName = registerUserDtoRequest.getLastName();
        String login = registerUserDtoRequest.getLogin();
        String password = registerUserDtoRequest.getPassword();
        return new User(firstName, lastName, login, password);
    }

}
