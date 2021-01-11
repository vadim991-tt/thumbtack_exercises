package net.thumbtack.school.concert.service.song;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.dto.request.song.AddSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddSongsDtoRequest;
import net.thumbtack.school.concert.dto.request.song.RateSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.RemoveSongDtoRequest;
import net.thumbtack.school.concert.dto.response.song.AddSongDtoResponse;
import net.thumbtack.school.concert.dto.response.song.AddSongsDtoResponse;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestSongOperations {
    @Test
    public void addSong() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Квадрат", 150);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Песня", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "ДругаяПесня", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Невладимирский централ", "Миха", "Круг", "Круг", 150);

        String responseSt = server.addSong(gson.toJson(request));
        String errorResponseSt1 = server.addSong(gson.toJson(request1));
        String responseSt2 = server.addSong(gson.toJson(request2));
        String responseSt3 = server.addSong(gson.toJson(request3));
        String responseSt4 = server.addSong(gson.toJson(request4));
        String responseSt5 = server.addSong(gson.toJson(request5));

        Set<Song> songsFromJson = new HashSet<>();
        songsFromJson.add(gson.fromJson(responseSt, AddSongDtoResponse.class).getSong());
        songsFromJson.add(gson.fromJson(responseSt2, AddSongDtoResponse.class).getSong());
        songsFromJson.add(gson.fromJson(responseSt3, AddSongDtoResponse.class).getSong());
        songsFromJson.add(gson.fromJson(responseSt4, AddSongDtoResponse.class).getSong());
        songsFromJson.add(gson.fromJson(responseSt5, AddSongDtoResponse.class).getSong());
        assertEquals(errorResponseSt1, "\"Владимирский централ - Круг is already added\"");
        assertEquals(DataBase.getDataBase().getSongs(), songsFromJson);
        server.stopServer(null);
    }

    @Test
    public void addSongErrors() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token, null, "Михаил Круг", "Круг", "Круг", 150);
        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Владимирский централ", null, "Круг", "Круг", 150);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", null, "Квадрат", 150);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Песня", "Миха", "Круг", null, 150);
        AddSongDtoRequest request4 = new AddSongDtoRequest(null, "ДругаяПесня", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "ДругаяПесня", "Миха", "Круг", "Круг", 678986);

        String errorResponseSt = server.addSong(gson.toJson(request));
        String errorResponseSt1 = server.addSong(gson.toJson(request1));
        String errorResponseSt2 = server.addSong(gson.toJson(request2));
        String errorResponseSt3 = server.addSong(gson.toJson(request3));
        String errorResponseSt4 = server.addSong(gson.toJson(request4));
        String errorResponseSt5 = server.addSong(gson.toJson(request5));

        assertAll(
                () -> assertEquals(errorResponseSt, "\"Song name not found\""),
                () -> assertEquals(errorResponseSt1, "\"Composer not found\""),
                () -> assertEquals(errorResponseSt2, "\"Song author not found\""),
                () -> assertEquals(errorResponseSt3, "\"Singer not found\""),
                () -> assertEquals(errorResponseSt4, "\"Invalid token\""),
                () -> assertEquals(errorResponseSt5, "\"Wrong song duration\"")
        );
        server.stopServer(null);
    }

    @Test
    public void addSongs() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Квадрат", 150);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Песня", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "ДругаяПесня", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Невладимирский централ", "Миха", "Круг", "Круг", 150);

        AddSongDtoRequest[] array = {request, request1, request2, request3, request4, request5};
        AddSongsDtoRequest requests = new AddSongsDtoRequest(array);
        String jsonRequest = gson.toJson(requests);
        String addSongs = server.addSongs(jsonRequest);
        String message = gson.fromJson(addSongs, AddSongsDtoResponse.class).getMessage();

        assertTrue(message.contains("5 out of 6 songs added, 1 error(s):"));
        assertTrue(message.contains("Владимирский централ - Круг is already added"));
        server.stopServer(null);
    }

    @Test
    public void addSongs1() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);
        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");

        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Квадрат", 150);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Песня", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "ДругаяПесня", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Невладимирский централ", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest[] array = {request, request2, request3, request4, request5};

        AddSongsDtoRequest requests = new AddSongsDtoRequest(array);
        String jsonRequest = gson.toJson(requests);
        String addSongs = server.addSongs(jsonRequest);
        String message = gson.fromJson(addSongs, AddSongsDtoResponse.class).getMessage();
        assertEquals(message, "5 out of 5 songs added, 0 error(s).");
        server.stopServer(null);
    }

    @Test
    public void addSongs2() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);
        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");

        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Владимирский централ", "Миха", "Круг", "Круг", 150);
        AddSongDtoRequest[] array = {request, request1, request2, request3, request4, request5};

        AddSongsDtoRequest requests = new AddSongsDtoRequest(array);
        String jsonRequest = gson.toJson(requests);
        String addSongs = server.addSongs(jsonRequest);
        String message = gson.fromJson(addSongs, AddSongsDtoResponse.class).getMessage();
        assertTrue(message.contains("1 out of 6 songs added, 5 error(s):"));
        assertTrue(message.contains("Владимирский централ - Круг is already added"));
        server.stopServer(null);
    }

    @Test
    public void addSongsError() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);
        AddSongsDtoRequest requests = new AddSongsDtoRequest(null);
        String jsonRequest = gson.toJson(requests);
        String error = server.addSongs(jsonRequest);
        assertEquals(error, "\"Request not found\"");
        server.stopServer(null);
    }

    @Test
    public void removeSong() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        server.addSong(gson.toJson(request));

        Set<Song> songs = new HashSet<>(DataBase.getDataBase().getSongs());
        RemoveSongDtoRequest removeRequest = new RemoveSongDtoRequest(token, "Владимирский централ - Круг");

        server.removeSong(gson.toJson(removeRequest));
        Set<Song> songs1 = new HashSet<>(DataBase.getDataBase().getSongs());

        assertNotEquals(songs, songs1);
        assertTrue(songs1.isEmpty());

        server.stopServer(null);
    }

    @Test
    public void removeSong1() throws IOException, SongException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        String respond1 = server.registerUser(gson.toJson(new User("Vasya", "Pupkin", "vasek1337", "SwordFish")));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();
        String respond2 = server.registerUser(gson.toJson(new User("Vasya", "Pupa", "vasek1334237", "SwordFish")));
        String token2 = gson.fromJson(respond2, RegisterUserDtoResponse.class).getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        Set<Song> songsBefore = new HashSet<>(DataBase.getDataBase().getSongs());
        server.addSong(gson.toJson(request));
        server.rateSong(gson.toJson(new RateSongDtoRequest(token2, "Владимирский централ - Круг", 5)));

        RemoveSongDtoRequest removeRequest2 = new RemoveSongDtoRequest(token1, "Владимирский централ - Круг");
        server.removeSong(gson.toJson(removeRequest2));
        Set<Song> songsAfter = new HashSet<>(DataBase.getDataBase().getSongs());

        assertNotEquals(songsBefore, songsAfter);
        assertEquals("Community", DataBase.getSongFromFullSongName("Владимирский централ - Круг").getRequestAuthor());
        server.stopServer(null);
    }

    @Test
    public void removeSongError() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        String respond1 = server.registerUser(gson.toJson(new User("Vasya", "Pupkin", "vasek1337", "SwordFish")));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();
        String respond2 = server.registerUser(gson.toJson(new User("Vasya", "Pupa", "vasek1334237", "SwordFish")));
        String token2 = gson.fromJson(respond2, RegisterUserDtoResponse.class).getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        server.addSong(gson.toJson(request));
        String invToken = UUID.randomUUID().toString();
        RemoveSongDtoRequest removeRequest = new RemoveSongDtoRequest(invToken, "Владимирский централ - Круг");

        String error1 = gson.fromJson(server.removeSong(gson.toJson(removeRequest)), String.class);
        String error2 = gson.fromJson(server.removeSong(gson.toJson(new RemoveSongDtoRequest(null, "Владимирский централ - Круг"))), String.class);
        String error3 = gson.fromJson(server.removeSong(gson.toJson(new RemoveSongDtoRequest(token1, null))), String.class);
        String error4 = gson.fromJson(server.removeSong(gson.toJson(new RemoveSongDtoRequest(token2, "Владимирский централ - Круг"))), String.class);

        assertAll(
                () -> assertEquals("Invalid token", error1),
                () -> assertEquals("Invalid token", error2),
                () -> assertEquals("Song not found", error3),
                () -> assertEquals("You cannot remove the song that was not added by you", error4),
                () -> assertFalse(DataBase.getDataBase().getSongs().isEmpty())
        );
        server.stopServer(null);
    }
}
