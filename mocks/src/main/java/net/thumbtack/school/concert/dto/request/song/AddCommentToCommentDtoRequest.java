package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.model.Comment;

public class AddCommentToCommentDtoRequest extends AddCommentToSongDtoRequest {
    private String previousComment;

    public AddCommentToCommentDtoRequest(String token, String song, String comment, String previousComment) {
        super(token, song, comment);
        this.previousComment = previousComment;
    }

    public String getPreviousComment() {
        return previousComment;
    }

    public void setPreviousComment(String previousComment) {
        this.previousComment = previousComment;
    }

    public static void validate(AddCommentToCommentDtoRequest request) throws SongException, UserException {
        AddCommentToSongDtoRequest.validate(request);
        if (request.getPreviousComment() == null || request.getPreviousComment().isBlank()) {
            throw new SongException(SongErrorCode.WRONG_PREV_COMMENT);
        }
    }

    public static Comment createCommentFromDto(AddCommentToCommentDtoRequest request) throws UserException {
        Comment comment = AddCommentToSongDtoRequest.createCommentFromDto(request);
        comment.setPreviousComment(request.getPreviousComment());
        return comment;
    }
}
