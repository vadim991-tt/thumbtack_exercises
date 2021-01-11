package net.thumbtack.school.concert.dto.response.song;

import net.thumbtack.school.concert.model.Song;

public class AddSongDtoResponse {
    private Song song;

    public AddSongDtoResponse(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
