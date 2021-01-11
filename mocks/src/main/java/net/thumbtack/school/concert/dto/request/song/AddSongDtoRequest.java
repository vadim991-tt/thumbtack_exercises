package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserErrorCode;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;

public class AddSongDtoRequest {
    private String token;
    private String songName;
    private String composer;
    private String songAuthor;
    private String singer; // Исполнитель
    private Integer duration;

    public AddSongDtoRequest(String token, String songName, String composer, String songAuthor, String singer, Integer duration) {
        this.token = token;
        this.songName = songName;
        this.composer = composer;
        this.songAuthor = songAuthor;
        this.singer = singer;
        this.duration = duration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getSongAuthor() {
        return songAuthor;
    }

    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public static void validate(AddSongDtoRequest request) throws SongException, UserException {
        String token = request.getToken();
        String songName = request.getSongName();
        String composer = request.getComposer();
        String songAuthor = request.getSongAuthor();
        String singer = request.getSinger();
        Integer duration = request.getDuration();
        if (token == null || token.isBlank()) {
            throw new UserException(UserErrorCode.INVALID_TOKEN);
        }
        if (songName == null || songName.isBlank())
            throw new SongException(SongErrorCode.SONGNAME_NOT_FOUND);
        if (composer == null || composer.isBlank())
            throw new SongException(SongErrorCode.COMPOSER_NOT_FOUND);
        if (songAuthor == null || songAuthor.isBlank())
            throw new SongException(SongErrorCode.AUTHOR_NOT_FOUND);
        if (singer == null || singer.isBlank())
            throw new SongException(SongErrorCode.SINGER_NOT_FOUND);
        if (duration == null || duration <= 60 || duration > 600)
            throw new SongException(SongErrorCode.WRONG_SONG_DURATION);
    }

    public static Song createSongFromDtoRequest(AddSongDtoRequest request) throws UserException {
        String songName = request.getSongName();
        String composer = request.getComposer();
        String songAuthor = request.getSongAuthor();
        String singer = request.getSinger();
        Integer duration = request.getDuration();
        return new Song(songName, composer, songAuthor, singer, null, duration);
    }
}
