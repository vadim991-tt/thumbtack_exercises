package net.thumbtack.school.concert.server;

import com.google.gson.Gson;

import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.dto.request.song.AddCommentToCommentDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddCommentToSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.RateSongDtoRequest;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.Rating;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class TestServer {
    @TempDir
    public Path tempDir;

    @Test
    public void startStopServer() throws IOException {
        Server server = new Server();
        server.startServer(null);
        Gson gson = new Gson();

        User firstUser = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        User secondUser = new User("Vasya", "Pupkin", "vasek1321", "SwordFish");
        User thirdUser = new User("Vasya", "Pupkin", "lalal2314", "1234LalaLala");
        User fourthUser = new User("Vasya", "Pupkin", "vasek13341", "MyPassword32");
        User fifthUser = new User("Vasya", "Pupkin", "vasek13321", "RibaMech123");
        User[] userArray = {firstUser, secondUser, thirdUser, fourthUser, fifthUser};


        for (User elem : userArray) {
            server.registerUser(gson.toJson(elem));
        }
        String sixResponse = server.registerUser(gson.toJson(new User("Vasya", "Singer", "vasek8888", "SwordFish")));
        String seventhResponse = server.registerUser(gson.toJson(new User("Admin", "Singer", "Admin321", "SwordFish")));
        String token6 = gson.fromJson(sixResponse, RegisterUserDtoResponse.class).getToken();
        String token7 = gson.fromJson(seventhResponse, RegisterUserDtoResponse.class).getToken();

        AddSongDtoRequest request = new AddSongDtoRequest(token6, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150);
        AddSongDtoRequest request1 = new AddSongDtoRequest(token6, "Владимирский централ", "Миха", "Круг", "Квадрат", 150);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token6, "Песня", "Миха", "Круг", "Круг", 150);

        server.addSong(gson.toJson(request));
        server.addSong(gson.toJson(request1));
        server.addSong(gson.toJson(request2));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token6, "Владимирский централ - Круг", "Крутая песня")));
        server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token7, "Владимирский централ - Круг", "Внатуре крутая песня", "Крутая песня")));
        server.rateSong(gson.toJson(new RateSongDtoRequest(token7, "Владимирский централ - Круг", 5)));

        Set<Song> firstSongSet = new HashSet<>(DataBase.getDataBase().getSongs());
        Set<User> firstUsers = new HashSet<>(DataBase.getDataBase().getUserSet());
        Map<String, String> firstLoginPassMap = new HashMap<>(DataBase.getDataBase().getPasswordByLogin());
        Map<String, Rating> firstRatings = new HashMap<>(DataBase.getDataBase().getRatings());
        File file = Files.createFile(tempDir.resolve("test.dat")).toFile();
        String fileName = file.getName();
        server.stopServer(fileName);

        server.startServer(fileName);
        Set<User> secondUsers = new HashSet<>(DataBase.getDataBase().getUserSet());
        Map<String, String> secondLoginPassMap = new HashMap<>(DataBase.getDataBase().getPasswordByLogin());
        Set<Song> secondSongSet = new HashSet<>(DataBase.getDataBase().getSongs());
        Map<String, Rating> secondRatings = new HashMap<>(DataBase.getDataBase().getRatings());
        Map<String, Map<String, Song>> songsByComp = DataBase.getDataBase().getSongsByComposer();

        assertAll(
                () -> assertEquals(firstSongSet, secondSongSet),
                () -> assertEquals(firstLoginPassMap, secondLoginPassMap),
                () -> assertEquals(firstUsers, secondUsers),
                () -> assertEquals(firstRatings, secondRatings)
        );
        server.stopServer(null);
    }

    @Test
    public void operatingOnServer() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        User user = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        try {
            server.registerUser(gson.toJson(user));
            fail();
        } catch (NullPointerException ignored) {

        }
        server.startServer(null);
        try {
            server.registerUser(gson.toJson(user));
        } catch (NullPointerException e) {
            fail();
        }
        server.stopServer(null);
        try {
            server.registerUser(gson.toJson(user));
            fail();
        } catch (NullPointerException ignored) {

        }
    }
}