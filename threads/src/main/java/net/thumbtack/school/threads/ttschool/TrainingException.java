package net.thumbtack.school.threads.ttschool;

public class TrainingException extends Exception {
    private TrainingErrorCode errorCode;

    public TrainingException(TrainingErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public TrainingException(TrainingErrorCode errorCode, String param) {
        super(String.format(errorCode.getErrorString(), param));
        this.errorCode = errorCode;

    }

    public TrainingErrorCode getErrorCode() {
        return errorCode;
    }
}
