package net.thumbtack.school.mock;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.dto.response.song.GetSongsDtoResponse;
import net.thumbtack.school.concert.model.Rating;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.sevice.SongService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TestSongService {

    @Test
    public void addSongResponseTest() {
        SongDao mockSongDao = mock(SongDao.class);

        SongService songService = new SongService();
        songService.setSongDao(mockSongDao);

        String jsonResp = songService.addSong(
                "{\"token\":\"123\",\"songName\":\"Владимирский централ\",\"composer\":\"Михаил Круг\",\"songAuthor\":\"Круг\",\"singer\":\"Круг\",\"duration\":150}");
        assertTrue(jsonResp.contains("song"));

    }

    @Test
    public void rateSongTest() throws SongException {
        SongDao mockSongDao = mock(SongDao.class);
        when(mockSongDao.addRating(any(Rating.class))).thenReturn(5);
        SongService songService = new SongService();
        songService.setSongDao(mockSongDao);
        String response = songService.rateSong(
                "{\"token\":\"1\",\"song\":\"Владимирский централ - Круг\",\"rating\":5}");
        assertTrue(response.contains("songRating"));
    }

    @Test
    public void removeSongTest() {
        SongDao mockSongDao = mock(SongDao.class);
        SongService songService = new SongService();
        songService.setSongDao(mockSongDao);
        String response = songService.removeSong(
                "{\"token\":\"10708f4b-2fb1-49e1-a7f4-089fd6d5d626\",\"song\":\"Владимирский централ - Круг\"}");
        assertTrue(response.contains("null"));
    }

    @Test
    public void removeRatingFromSongTest() {
        SongDao mockSongDao = mock(SongDao.class);

        SongService songService = new SongService();
        songService.setSongDao(mockSongDao);

        String response = songService.removeRating("{\"token\":\"1\",\"song\":\"Владимирский централ - Круг\"}");
        assertTrue(response.contains("songRating"));

    }

    @Test
    public void changeRatingTest() throws SongException {
        SongDao mockSongDao = mock(SongDao.class);
        when(mockSongDao.changeRating(any(Rating.class))).thenReturn(555);

        SongService songService = new SongService();
        songService.setSongDao(mockSongDao);

        String response = songService.changeRating("{\"token\":\"1\",\"song\":\"Владимирский централ - Круг\",\"rating\":5}");
        assertTrue(response.contains("songRating"));

    }

    @Test
    public void getSongSetTest() throws UserException {
        SongDao mockSongDao = mock(SongDao.class);
        SongService songService = new SongService();
        songService.setSongDao(mockSongDao);

        Song firstSong = new Song("SongName1", "Composer", "songAuthor", "Singer", null, 150);
        Song secondSong = new Song("SongName2", "Composer", "songAuthor", "Singer", null, 150);
        Song thirdSong = new Song("SongName3", "Composer", "songAuthor", "Singer", null, 150);
        Set<Song> songs = new HashSet<>(Arrays.asList(firstSong, secondSong, thirdSong));

        when(mockSongDao.getSongSet(anyString())).thenReturn(songs);
        String response = songService.getSongSet("{\"token\":\"1\"}");
        Gson gson = new Gson();

        GetSongsDtoResponse getSongsDtoResponse = gson.fromJson(response, GetSongsDtoResponse.class);
        Set<Song> songsFromDB = getSongsDtoResponse.getSongs();

        assertEquals(songs, songsFromDB);
    }

}
