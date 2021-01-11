package net.thumbtack.school.notes.base;

public enum ServerErrorCode {
    LOGIN_ERROR("Can't login into account. Please check that your username and password are correct."),
    PERMISSION_ERROR("Can not give user super privileges. You are not super user"),
    INVALID_TOKEN("Invalid token"),
    INVALID_LOGIN("Invalid login"),
    INVALID_FIRSTNAME("Invalid first name"),
    INVALID_LASTNAME("Invalid last name"),
    INVALID_PATRONYMIC("Invalid patronymic"),
    INVALID_PASSWORD("Invalid password"),
    INVALID_SECTION("Request contains invalid fields."),
    INVALID_CHANGE_NOTE_REQUEST("Can not move or rename note, request contains invalid fields"),
    DATABASE_ERROR("Database error"),
    INVALID_SECTION_NAME("Section %s already exists"),
    LOGIN_ALREADY_EXISTS("User %s already exists");

    private final String message;

    ServerErrorCode(String errorString) {
        this.message = errorString;
    }

    public String getErrorString() {
        return message;
    }


}
