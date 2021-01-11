package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.model.Comment;

public class ChangeCommentDtoRequest extends AddCommentToSongDtoRequest {
    private String commentToChange;

    public ChangeCommentDtoRequest(String token, String song, String comment, String previousComment) {
        super(token, song, comment);
        this.commentToChange = previousComment;
    }

    public String getCommentToChange() {
        return commentToChange;
    }

    public void setCommentToChange(String commentToChange) {
        this.commentToChange = commentToChange;
    }

    public static void validate(ChangeCommentDtoRequest request) throws SongException, UserException {
        AddCommentToSongDtoRequest.validate(request);
        if (request.getCommentToChange() == null || request.getCommentToChange().isBlank()) {
            throw new SongException(SongErrorCode.WRONG_PREV_COMMENT);
        }
    }

    public static Comment createCommentFromDto(ChangeCommentDtoRequest request) throws UserException {
        Comment comment = AddCommentToSongDtoRequest.createCommentFromDto(request);
        comment.setPreviousComment(request.getCommentToChange());
        return comment;
    }
}
