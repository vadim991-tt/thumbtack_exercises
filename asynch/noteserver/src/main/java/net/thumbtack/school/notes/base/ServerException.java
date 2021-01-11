package net.thumbtack.school.notes.base;

public class ServerException extends Exception {
    private final ServerErrorCode errorCode;
    private String field;

    public ServerException(ServerErrorCode errorCode) {
        super(errorCode.getErrorString());
        this.errorCode = errorCode;
    }

    public ServerException(ServerErrorCode errorCode, String param, String field) {
        super(String.format(errorCode.getErrorString(), param));
        this.errorCode = errorCode;
        this.field = field;
    }

    public ServerErrorCode getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }

}
