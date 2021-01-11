package net.thumbtack.school.concert.dto.response.song;

import net.thumbtack.school.concert.model.Song;

import java.util.Set;

public class GetSongsDtoResponse {
    private Set<Song> songs;

    public GetSongsDtoResponse(Set<Song> songs) {
        this.songs = songs;
    }

    public Set<Song> getSongs() {
        return songs;
    }
}
