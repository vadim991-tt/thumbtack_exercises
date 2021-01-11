package net.thumbtack.school.concert.base.song;

public class SongException extends Exception {
    private SongErrorCode errorCode;

    public SongException(SongErrorCode errorCode) {
        super(errorCode.getErrorString());
        this.errorCode = errorCode;
    }

    public SongException(SongErrorCode errorCode, String param) {
        super(String.format(errorCode.getErrorString(), param));
        this.errorCode = errorCode;
    }
}
