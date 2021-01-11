package net.thumbtack.school.concert.service.song;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.dto.request.song.AddSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.RateSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.RemoveRatingDtoRequest;
import net.thumbtack.school.concert.dto.request.song.RemoveSongDtoRequest;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestRatingOperations {
    @Test
    public void addRating() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        User user2 = new User("Ilya", "Punya", "IElogine", "SwordFish");

        String respond1 = server.registerUser(gson.toJson(user1));
        String respond2 = server.registerUser(gson.toJson(user2));

        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();
        String token2 = gson.fromJson(respond2, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        String error1 = server.rateSong(gson.toJson(new RateSongDtoRequest(token1, "Владимирский централ - Круг", 5)));

        server.rateSong(gson.toJson(new RateSongDtoRequest(token2, "Владимирский централ - Круг", 5)));
        String error2 = server.rateSong(gson.toJson(new RateSongDtoRequest(token2, "Владимирский централ - Круг", 5)));

        RemoveSongDtoRequest removeRating = new RemoveSongDtoRequest(token2, "Владимирский централ - Круг");
        server.removeRating(gson.toJson(removeRating));
        String error3 = server.removeRating(gson.toJson(removeRating));

        assertAll(
                () -> assertEquals(error1, "\"You cannot rate the song you added\""),
                () -> assertEquals(error2, "\"Song is already rated\""),
                () -> assertEquals(error3, "\"Rating not found\""),
                () -> assertTrue(DataBase.getDataBase().getRatings().isEmpty())
        );
        server.stopServer(null);
    }

    @Test
    public void addRatingErrors() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);
        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");

        String respond1 = server.registerUser(gson.toJson(user1));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();
        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));

        String jsonError1 = server.rateSong(gson.toJson(new RateSongDtoRequest(null, "Владимирский централ - Круг", 5)));
        String jsonError2 = server.rateSong(gson.toJson(new RateSongDtoRequest(token1, null, 5)));
        String jsonError3 = server.rateSong(gson.toJson(new RateSongDtoRequest(token1, "Владимирский централ - Круг", null)));
        String jsonError4 = server.rateSong(gson.toJson(new RateSongDtoRequest(token1, "Владимирский централ - Круг", -4)));
        String jsonError5 = server.rateSong(gson.toJson(new RateSongDtoRequest(token1, "Владимирский централ - Круг", 12)));
        assertAll(
                () -> assertEquals("\"Invalid token\"", jsonError1),
                () -> assertEquals("\"Song not found\"", jsonError2),
                () -> assertEquals("\"Wrong rating\"", jsonError3),
                () -> assertEquals("\"Wrong rating\"", jsonError4),
                () -> assertEquals("\"Wrong rating\"", jsonError5)
        );
        server.stopServer(null);
    }

    @Test
    public void removeRating() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        User user2 = new User("Ilya", "Punya", "IElogine", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        String respond2 = server.registerUser(gson.toJson(user2));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();
        String token2 = gson.fromJson(respond2, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));

        assertFalse(DataBase.getDataBase().getSongs().isEmpty());

        server.rateSong(gson.toJson(new RateSongDtoRequest(token2, "Владимирский централ - Круг", 5)));
        server.removeSong(gson.toJson(new RemoveSongDtoRequest(token1, "Владимирский централ - Круг")));
        server.removeRating(gson.toJson(new RemoveRatingDtoRequest(token2, "Владимирский централ - Круг")));
        Map<String, Map<String, Song>> map = DataBase.getDataBase().getSongsByRequester();

        assertTrue(DataBase.getDataBase().getSongs().isEmpty());

        server.stopServer(null);
    }

    @Test
    public void changeRating() throws IOException, SongException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        User user2 = new User("Ilya", "Punya", "IElogine", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        String respond2 = server.registerUser(gson.toJson(user2));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();
        String token2 = gson.fromJson(respond2, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.rateSong(gson.toJson(new RateSongDtoRequest(token2, "Владимирский централ - Круг", 5)));
        server.changeRating(gson.toJson(new RateSongDtoRequest(token2, "Владимирский централ - Круг", 3)));

        Song song = DataBase.getSongFromFullSongName("Владимирский централ - Круг");
        int rating = song.getRating();
        int rates = song.getRates();

        assertAll(
                () -> assertEquals(rating, 8),
                () -> assertEquals(rates, 2),
                () -> assertEquals(DataBase.getDataBase().getRatings().size(), 1)
        );
        server.stopServer(null);
    }

    @Test
    public void changeRating1() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        User user2 = new User("Ilya", "Punya", "IElogine", "SwordFish");
        User user3 = new User("Vladimir", "Putin", "PatriotRossii", "SwordFish");

        String token1 = gson.fromJson(server.registerUser(gson.toJson(user1)), RegisterUserDtoResponse.class).getToken();
        String token2 = gson.fromJson(server.registerUser(gson.toJson(user2)), RegisterUserDtoResponse.class).getToken();
        String token3 = gson.fromJson(server.registerUser(gson.toJson(user3)), RegisterUserDtoResponse.class).getToken();
        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.rateSong(gson.toJson(new RateSongDtoRequest(token2, "Владимирский централ - Круг", 5)));
        String jsonError = server.changeRating(gson.toJson(new RateSongDtoRequest(token3, "Владимирский централ - Круг", 3)));

        assertEquals(jsonError, "\"Rating not found\"");
        server.stopServer(null);
    }

    @Test
    public void removeRatingErrors() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        String respond1 = server.registerUser(gson.toJson(new User("Vasya", "Pupkin", "vasek1337", "SwordFish")));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();
        String jsonError1 = server.removeRating(gson.toJson(new RemoveRatingDtoRequest(null, "Владимирский централ - Круг")));
        String jsonError2 = server.removeRating(gson.toJson(new RemoveRatingDtoRequest(token1, null)));

        assertEquals(jsonError1, "\"Invalid token\"");
        assertEquals(jsonError2, "\"Song not found\"");
        server.stopServer(null);
    }
}
