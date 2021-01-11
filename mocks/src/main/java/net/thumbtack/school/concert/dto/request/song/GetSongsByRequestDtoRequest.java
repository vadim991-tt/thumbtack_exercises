package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;

public class GetSongsByRequestDtoRequest {
    private String token;
    private String requestString;

    public GetSongsByRequestDtoRequest(String token, String requestString) {
        this.token = token;
        this.requestString = requestString;
    }

    public String getRequestString() {
        return requestString;
    }

    public String getToken() {
        return token;
    }

    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static void validate(GetSongsByRequestDtoRequest request) throws UserException, SongException {
        if (request.getToken() == null || request.getToken().isBlank())
            throw new UserException(net.thumbtack.school.concert.base.user.UserErrorCode.INVALID_TOKEN);
        if (request.getRequestString() == null || request.getRequestString().isBlank())
            throw new SongException(SongErrorCode.REQUEST_NOT_FOUND);
    }
}
