package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserErrorCode;
import net.thumbtack.school.concert.base.user.UserException;

public class RemoveRatingDtoRequest {
    private String token;
    private String song;

    public RemoveRatingDtoRequest(String token, String song) {
        this.token = token;
        this.song = song;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSong() {
        return song;
    }

    public static void validate(RemoveRatingDtoRequest request) throws SongException, UserException {
        String token = request.getToken();
        String song = request.getSong();
        if (token == null || token.isBlank())
            throw new UserException(UserErrorCode.INVALID_TOKEN);
        if (song == null || song.isBlank())
            throw new SongException(SongErrorCode.SONG_NOT_FOUND);

    }
}
