package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserErrorCode;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.model.Rating;


public class RateSongDtoRequest {
    private String token;
    private String song;
    private Integer rating;

    public RateSongDtoRequest(String token, String song, Integer rating) {
        this.token = token;
        this.song = song;
        this.rating = rating;
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

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public static void validate(RateSongDtoRequest request) throws SongException, UserException {
        String token = request.getToken();
        Integer rating = request.getRating();
        String song = request.getSong();
        if (song == null || song.isBlank())
            throw new SongException(SongErrorCode.SONG_NOT_FOUND);
        if (token == null || token.isBlank())
            throw new UserException(UserErrorCode.INVALID_TOKEN);
        if (rating == null || rating < 1 || rating > 5)
            throw new SongException(SongErrorCode.WRONG_RATING);

    }

    public static Rating createRatingFromDTO(RateSongDtoRequest request) throws UserException {
        return new Rating(null, request.getSong(), request.getRating());
    }
}
