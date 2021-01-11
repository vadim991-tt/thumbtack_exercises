package net.thumbtack.school.concert.service.song;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.dto.request.song.AddSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddSongsDtoRequest;
import net.thumbtack.school.concert.dto.request.song.GetSongsByRequestDtoRequest;
import net.thumbtack.school.concert.dto.response.song.GetSongsDtoResponse;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestGetSongs {
    @Test
    public void getSongs() throws IOException {
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
        server.addSongs(jsonRequest);

        Set<Song> songsFromDataBase = DataBase.getDataBase().getSongs();
        String stringSet = server.getSongSet(gson.toJson(response));

        Set<Song> songsFromJson = gson.fromJson(stringSet, GetSongsDtoResponse.class).getSongs();
        assertEquals(songsFromDataBase, songsFromJson);

        server.stopServer(null);
    }

    @Test
    public void getSongsByComposer() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Пачка сигарет", "Цой", "Цой и КО", "Кино", 150);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Центральный Владимир", "Михаил Круг", "Круг", "Квадрат (брат Михаила)", 150);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Симфония 40", "Вольфганг Амадей Моцарт", "Моцарт", "Моцарт", 150);
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "Кукушка", "Цой", "Цой и КО", "Кино", 150);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Щелкунчик", "Чайковский", "Чайковский", "Петр Чайковский", 150);
        AddSongDtoRequest[] array = {request, request1, request2, request3, request4, request5};
        AddSongsDtoRequest requests = new AddSongsDtoRequest(array);

        String jsonRequest = gson.toJson(requests);
        server.addSongs(jsonRequest);

        String respond1 = server.getSongsByComposer(gson.toJson(new GetSongsByRequestDtoRequest(token, "Цой")));
        Set<Song> songsFromJson1 = gson.fromJson(respond1, GetSongsDtoResponse.class).getSongs();

        String respond2 = server.getSongsByComposer(gson.toJson(new GetSongsByRequestDtoRequest(token, "Михаил Круг")));
        Set<Song> songsFromJson2 = gson.fromJson(respond2, GetSongsDtoResponse.class).getSongs();

        String respond3 = server.getSongsByComposer(gson.toJson(new GetSongsByRequestDtoRequest(token, "Чайковский")));
        Set<Song> songsFromJson3 = gson.fromJson(respond3, GetSongsDtoResponse.class).getSongs();

        String respond4 = server.getSongsByComposer(gson.toJson(new GetSongsByRequestDtoRequest(token, "Вольфганг Амадей Моцарт")));
        Set<Song> songsFromJson4 = gson.fromJson(respond4, GetSongsDtoResponse.class).getSongs();

        String respond5 = server.getSongsByComposer(gson.toJson(new GetSongsByRequestDtoRequest(token, "Сергей Рахманинов")));
        Set<Song> songsFromJson5 = gson.fromJson(respond5, GetSongsDtoResponse.class).getSongs();

        assertAll(
                () -> assertEquals(songsFromJson1.size(), 2),
                () -> assertEquals(songsFromJson2.size(), 2),
                () -> assertEquals(songsFromJson3.size(), 1),
                () -> assertEquals(songsFromJson4.size(), 1),
                () -> assertEquals(songsFromJson5.size(), 0)
        );

        server.stopServer(null);
    }

    @Test
    public void getSongsBySongAuthor() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        Set<Song> songs = DataBase.getDataBase().getSongs();
        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Пачка сигарет", "Цой", "Цой и КО", "Кино", 150);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Центральный Владимир", "Михаил Круг", "Круг", "Квадрат (брат Михаила)", 150);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Симфония 40", "Вольфганг Амадей Моцарт", "Моцарт", "Моцарт", 150);
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "Кукушка", "Цой", "Цой и КО", "Кино", 150);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Щелкунчик", "Чайковский", "Чайковский", "Петр Чайковский", 150);
        AddSongDtoRequest[] array = {request, request1, request2, request3, request4, request5};
        AddSongsDtoRequest requests = new AddSongsDtoRequest(array);

        String jsonRequest = gson.toJson(requests);
        server.addSongs(jsonRequest);
        String respond1 = server.getSongsBySongAuthor(gson.toJson(new GetSongsByRequestDtoRequest(token, "Цой и КО")));
        Set<Song> songsFromJson1 = gson.fromJson(respond1, GetSongsDtoResponse.class).getSongs();
        String respond2 = server.getSongsBySongAuthor(gson.toJson(new GetSongsByRequestDtoRequest(token, "Круг")));
        Set<Song> songsFromJson2 = gson.fromJson(respond2, GetSongsDtoResponse.class).getSongs();
        String respond3 = server.getSongsBySongAuthor(gson.toJson(new GetSongsByRequestDtoRequest(token, "Чайковский")));
        Set<Song> songsFromJson3 = gson.fromJson(respond3, GetSongsDtoResponse.class).getSongs();
        String respond4 = server.getSongsBySongAuthor(gson.toJson(new GetSongsByRequestDtoRequest(token, "Моцарт")));
        Set<Song> songsFromJson4 = gson.fromJson(respond4, GetSongsDtoResponse.class).getSongs();
        String respond5 = server.getSongsBySongAuthor(gson.toJson(new GetSongsByRequestDtoRequest(token, "Сергей Рахманинов")));
        Set<Song> songsFromJson5 = gson.fromJson(respond5, GetSongsDtoResponse.class).getSongs();
        String respond6 = server.getSongsBySongAuthor(gson.toJson(new GetSongsByRequestDtoRequest(token, "Цой")));
        Set<Song> songsFromJson6 = gson.fromJson(respond6, GetSongsDtoResponse.class).getSongs();
        assertAll(
                () -> assertEquals(songsFromJson1.size(), 2),
                () -> assertEquals(songsFromJson2.size(), 2),
                () -> assertEquals(songsFromJson3.size(), 1),
                () -> assertEquals(songsFromJson4.size(), 1),
                () -> assertEquals(songsFromJson5.size(), 0),
                () -> assertEquals(songsFromJson6.size(), 0)
        );
        server.stopServer(null);
    }

    @Test
    public void getSongsBySinger() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Пачка сигарет", "Цой", "Цой и КО", "Кино", 150);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Центральный Владимир", "Михаил Круг", "Круг", "Квадрат (брат Михаила)", 150);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Симфония 40", "Вольфганг Амадей Моцарт", "Моцарт", "Моцарт", 150);
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "Кукушка", "Цой", "Цой и КО", "Кино", 150);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Щелкунчик", "Чайковский", "Чайковский", "Петр Чайковский", 150);
        AddSongDtoRequest[] array = {request, request1, request2, request3, request4, request5};
        AddSongsDtoRequest requests = new AddSongsDtoRequest(array);
        String jsonRequest = gson.toJson(requests);
        server.addSongs(jsonRequest);

        String respond1 = server.getSongsBySinger(gson.toJson(new GetSongsByRequestDtoRequest(token, "Кино")));
        Set<Song> songsFromJson1 = gson.fromJson(respond1, GetSongsDtoResponse.class).getSongs();

        String respond2 = server.getSongsBySinger(gson.toJson(new GetSongsByRequestDtoRequest(token, "Круг")));
        Set<Song> songsFromJson2 = gson.fromJson(respond2, GetSongsDtoResponse.class).getSongs();

        String respond3 = server.getSongsBySinger(gson.toJson(new GetSongsByRequestDtoRequest(token, "Петр Чайковский")));
        Set<Song> songsFromJson3 = gson.fromJson(respond3, GetSongsDtoResponse.class).getSongs();

        String respond4 = server.getSongsBySinger(gson.toJson(new GetSongsByRequestDtoRequest(token, "Моцарт")));
        Set<Song> songsFromJson4 = gson.fromJson(respond4, GetSongsDtoResponse.class).getSongs();

        String respond5 = server.getSongsBySinger(gson.toJson(new GetSongsByRequestDtoRequest(token, "Сергей Рахманинов")));
        Set<Song> songsFromJson5 = gson.fromJson(respond5, GetSongsDtoResponse.class).getSongs();

        String respond6 = server.getSongsBySinger(gson.toJson(new GetSongsByRequestDtoRequest(token, "Цой")));
        Set<Song> songsFromJson6 = gson.fromJson(respond6, GetSongsDtoResponse.class).getSongs();

        assertAll(
                () -> assertEquals(songsFromJson1.size(), 2),
                () -> assertEquals(songsFromJson2.size(), 1),
                () -> assertEquals(songsFromJson3.size(), 1),
                () -> assertEquals(songsFromJson4.size(), 1),
                () -> assertEquals(songsFromJson5.size(), 0),
                () -> assertEquals(songsFromJson6.size(), 0)
        );
        server.stopServer(null);
    }

    @Test
    public void getSongsError() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);
        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond, RegisterUserDtoResponse.class);

        String token = response.getToken();
        String error1 = server.getSongsBySinger(gson.toJson(new GetSongsByRequestDtoRequest(null, "Кино")));
        String error2 = server.getSongsBySinger(gson.toJson(new GetSongsByRequestDtoRequest(token, null)));
        String error3 = server.getSongsByComposer(gson.toJson(new GetSongsByRequestDtoRequest(token, null)));
        String error4 = server.getSongsBySongAuthor(gson.toJson(new GetSongsByRequestDtoRequest(token, null)));
        response.setToken(null);
        String error5 = server.getSongSet(gson.toJson(response));
        assertAll(
                () -> assertEquals(error1, "\"Invalid token\""),
                () -> assertEquals(error2, "\"Request not found\""),
                () -> assertEquals(error3, "\"Request not found\""),
                () -> assertEquals(error4, "\"Request not found\""),
                () -> assertEquals(error5, "\"Invalid token\"")
        );
        server.stopServer(null);
    }
}
