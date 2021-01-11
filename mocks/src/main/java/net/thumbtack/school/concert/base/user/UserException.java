package net.thumbtack.school.concert.base.user;

public class UserException extends Exception {
    private UserErrorCode errorCode;

    public UserException(UserErrorCode errorCode) {
        super(errorCode.getErrorString());
        this.errorCode = errorCode;
    }

    public UserException(UserErrorCode errorCode, String param) {
        super(String.format(errorCode.getErrorString(), param));
        this.errorCode = errorCode;
    }
}
