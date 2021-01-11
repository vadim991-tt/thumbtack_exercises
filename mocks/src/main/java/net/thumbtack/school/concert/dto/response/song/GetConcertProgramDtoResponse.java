package net.thumbtack.school.concert.dto.response.song;

import net.thumbtack.school.concert.model.ConcertSong;

import java.util.Set;

public class GetConcertProgramDtoResponse {
    private Set<ConcertSong> songs;

    public GetConcertProgramDtoResponse(Set<ConcertSong> songs) {
        this.songs = songs;
    }

    public Set<ConcertSong> getSongs() {
        return songs;
    }
}
