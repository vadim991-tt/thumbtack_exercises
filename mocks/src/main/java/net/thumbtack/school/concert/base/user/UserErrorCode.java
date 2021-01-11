package net.thumbtack.school.concert.base.user;

public enum UserErrorCode {
    USER_WRONG_FIRSTNAME("Can not register user: wrong first name %s"),
    USER_WRONG_LASTNAME("Can not register user: wrong last name %s"),
    USER_WRONG_LOGIN("Can not register user: wrong login %s"),
    USER_WRONG_PASSWORD("Can not register user: wrong password"),
    USER_LOGIN_ALREADY_TAKEN("Can not register user: %s login is already taken"),
    LOGIN_ERROR("Wrong login"),
    PASSWORD_ERROR("Wrong password"),
    INVALID_TOKEN("Invalid token");

    private String errorMessage;

    UserErrorCode(String errorString) {
        this.errorMessage = errorString;
    }

    public String getErrorString() {
        return errorMessage;
    }
}
