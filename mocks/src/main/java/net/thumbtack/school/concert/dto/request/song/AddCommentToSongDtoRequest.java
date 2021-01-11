package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserErrorCode;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.model.Comment;

public class AddCommentToSongDtoRequest {
    private String token;
    private String song;
    private String comment;

    public AddCommentToSongDtoRequest(String token, String song, String comment) {
        this.token = token;
        this.song = song;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public String getSong() {
        return song;
    }

    public String getToken() {
        return token;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static void validate(AddCommentToSongDtoRequest request) throws UserException, SongException {
        if (request.getToken() == null || request.getToken().isBlank())
            throw new UserException(UserErrorCode.INVALID_TOKEN);
        if (request.getSong() == null || request.getSong().isBlank())
            throw new SongException(SongErrorCode.SONG_NOT_FOUND);
        if (request.getComment() == null || request.getComment().isBlank())
            throw new SongException(SongErrorCode.WRONG_COMMENT);
    }

    public static Comment createCommentFromDto(AddCommentToSongDtoRequest request) throws UserException {
        ;
        String song = request.getSong();
        String text = request.getComment();
        return new Comment(null, text, song);
    }
}
