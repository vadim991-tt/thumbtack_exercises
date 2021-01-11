package net.thumbtack.school.threads.ttschool;

public enum TrainingErrorCode {
    // Добавил %s как показано в лекции, чтобы можно было получить текст ошибки через .getMessage
    // проверил на Demo, работает
    TRAINEE_WRONG_FIRSTNAME("Wrong first name %s"),
    TRAINEE_WRONG_LASTNAME("Wrong last name %s"),
    TRAINEE_WRONG_RATING("Wrong rating %s"),
    GROUP_WRONG_NAME("Wrong group name %s"),
    GROUP_WRONG_ROOM("Wrong group room %s"),
    TRAINEE_NOT_FOUND("Trainee not found"),
    SCHOOL_WRONG_NAME("Wrong school name"),
    DUPLICATE_GROUP_NAME("Group name can not be duplicated"),
    GROUP_NOT_FOUND("Group not found"),
    DUPLICATE_TRAINEE("Trainee can not be duplicated"),
    EMPTY_TRAINEE_QUEUE("Trainee queue is empty");

    private String errorMessage;

    private TrainingErrorCode(String errorString) {
        this.errorMessage = errorString;
    }

    public String getErrorString() {
        return errorMessage;
    }
}
