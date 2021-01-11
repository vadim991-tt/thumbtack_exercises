package net.thumbtack.school.concert.dto.response.song;

public class RateSongDtoResponse {
    private int songRating;

    public RateSongDtoResponse(int songRating) {
        this.songRating = songRating;
    }

    public int getSongRating() {
        return songRating;
    }

    public void setSongRating(int songRating) {
        this.songRating = songRating;
    }
}
