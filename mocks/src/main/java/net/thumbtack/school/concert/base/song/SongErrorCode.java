package net.thumbtack.school.concert.base.song;

public enum SongErrorCode {
    SONG_ALREADY_ADDED("%s is already added"),
    SONG_NOT_FOUND("Song not found"),
    SONGNAME_NOT_FOUND("Song name not found"),
    COMPOSER_NOT_FOUND("Composer not found"),
    AUTHOR_NOT_FOUND("Song author not found"),
    SINGER_NOT_FOUND("Singer not found"),
    WRONG_SONG_DURATION("Wrong song duration"),
    WRONG_RATING("Wrong rating"),
    RATING_ERROR("Song is already rated"),
    RATING_AUTHOR_ERROR("You cannot rate the song you added"),
    REMOVE_SONG_ERROR("You cannot remove the song that was not added by you"),
    REMOVE_RATING_ERROR("Rating not found"),
    WRONG_COMMENT("Comment not found"),
    COMMENT_ERROR("This comment is already added"),
    WRONG_PREV_COMMENT("Previous comment not found"),
    REQUEST_NOT_FOUND("Request not found");

    private String errorMessage;

    SongErrorCode(String errorString) {
        this.errorMessage = errorString;
    }

    public String getErrorString() {
        return errorMessage;
    }
}
