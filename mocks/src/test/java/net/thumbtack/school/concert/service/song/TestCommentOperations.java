package net.thumbtack.school.concert.service.song;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.dto.request.song.AddCommentToCommentDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddCommentToSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.ChangeCommentDtoRequest;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestCommentOperations {
    @Test
    public void addCommentToSong() throws IOException, SongException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        String respond1 = server.registerUser(gson.toJson(
                new User("Vasya", "Pupkin", "vasek1337", "SwordFish")));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();
        server.addSong(gson.toJson(
                new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addCommentToSong(gson.toJson(
                new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        String jsonError = server.addCommentToSong(gson.toJson(
                new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));

        assertEquals(gson.fromJson(jsonError, String.class), "This comment is already added");
        assertEquals(1, DataBase.getSongFromFullSongName("Владимирский централ - Круг").getComments().size());
        server.stopServer(null);
    }

    @Test
    public void addCommentToComment() throws IOException, SongException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(
                new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addCommentToSong(gson.toJson(
                new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        server.addCommentToComment(gson.toJson(
                new AddCommentToCommentDtoRequest(token1, "Владимирский централ - Круг", "Крутяк!!", "Крутая песня")));
        String jsonError1 = server.addCommentToComment(gson.toJson(
                new AddCommentToCommentDtoRequest(token1, "Владимирский централ - Круг", "Крутяк!!", "Этого комментария не существует")));
        String jsonError2 = server.addCommentToComment(gson.toJson(
                new AddCommentToCommentDtoRequest(token1, "Владимирский централ - Круг", "Крутяк!!", "Крутая песня")));

        Song song = DataBase.getSongFromFullSongName("Владимирский централ - Круг");
        for (Comment elem : song.getComments()) {
            if (elem.getAuthor().equals("vasek1337") && elem.getText().equals("Крутая песня")) {
                assertEquals(elem.getComments().size(), 1);
            }
        }
        assertEquals(jsonError1, "\"Previous comment not found\"");
        assertEquals(jsonError2, "\"This comment is already added\"");
        server.stopServer(null);
    }

    @Test
    public void addCommentErrors() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        String stringJson1 = server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(null, "Владимирский централ - Круг", "Крутяк!!", "Крутая песня")));
        String stringJson2 = server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token1, null, "Крутяк!!", "Крутая песня")));
        String stringJson3 = server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token1, "Владимирский централ - Круг", null, "Крутая песня")));
        String stringJson4 = server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token1, "Владимирский централ - Круг", "Крутяк!!", null)));

        assertAll(
                () -> assertEquals("\"Invalid token\"", stringJson1),
                () -> assertEquals("\"Song not found\"", stringJson2),
                () -> assertEquals("\"Comment not found\"", stringJson3),
                () -> assertEquals("\"Previous comment not found\"", stringJson4)
        );
        server.stopServer(null);
    }

    @Test
    public void removeCommentFromSong() throws IOException, SongException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token1, "Владимирский централ - Круг", "Крутяк!", "Крутая песня")));
        server.deleteCommentFromSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));

        Song song = DataBase.getSongFromFullSongName("Владимирский централ - Круг");
        for (Comment elem : song.getComments()) {
            assertEquals(elem.getAuthor(), "Community");
            assertEquals(elem.getComments().size(), 1);
        }
        server.stopServer(null);
    }

    @Test
    public void removeCommentFromSong1() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        String errorJson = server.deleteCommentFromSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Этого комментария не существует")));
        server.deleteCommentFromSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));

        assertEquals(errorJson, "\"Comment not found\"");
        server.stopServer(null);
    }

    @Test
    public void changeComment() throws IOException, SongException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token1, "Владимирский централ - Круг", "Отличная Песня!", "Крутая песня")));
        server.changeComment(gson.toJson(new ChangeCommentDtoRequest(token1, "Владимирский централ - Круг", "Я подумал и песня так себе", "Крутая песня")));
        String errorJson = server.changeComment(gson.toJson(new ChangeCommentDtoRequest(token1, "Владимирский централ - Круг", "Я подумал и песня так себе", "Этого комментария не существует")));

        assertEquals(errorJson, "\"Comment not found\"");
        assertEquals(DataBase.getSongFromFullSongName("Владимирский централ - Круг").getComments().size(), 2);
        server.stopServer(null);
    }

    @Test
    public void changeComment1() throws IOException, SongException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        server.changeComment(gson.toJson(new ChangeCommentDtoRequest(token1, "Владимирский централ - Круг", "Я подумал и песня так себе", "Крутая песня")));

        assertEquals(DataBase.getSongFromFullSongName("Владимирский централ - Круг").getComments().size(), 1);
        server.stopServer(null);
    }

    @Test
    public void changeCommentErrors() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        String jsonError = server.changeComment(gson.toJson(new ChangeCommentDtoRequest(token1, "Владимирский централ - Круг", "Я подумал и песня так себе", null)));

        assertEquals(jsonError, "\"Previous comment not found\"");
        server.stopServer(null);
    }

    @Test
    public void removeCommentFromComment() throws IOException, SongException {
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
        Song song = DataBase.getSongFromFullSongName("Владимирский централ - Круг");
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token2, "Владимирский централ - Круг", "Действительно, отличная песня!", "Крутая песня")));

        Set<Comment> comments = song.getComments();
        for (Comment elem : comments)
            assertFalse(elem.getComments().isEmpty());
        server.deleteCommentFromComment(gson.toJson((new AddCommentToCommentDtoRequest(token2, "Владимирский централ - Круг", "Действительно, отличная песня!", "Крутая песня"))));
        for (Comment elem : comments)
            assertTrue(elem.getComments().isEmpty());
        server.stopServer(null);
    }

    @Test
    public void removeCommentFromComment1() throws IOException {
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
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token1, "Владимирский централ - Круг", "Крутая песня")));
        server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token2, "Владимирский централ - Круг", "Действительно, отличная песня!", "Крутая песня")));
        server.deleteCommentFromComment(gson.toJson((new AddCommentToCommentDtoRequest(token2, "Владимирский централ - Круг", "Действительно, отличная песня!", "Крутая песня"))));
        String errorJson1 = server.deleteCommentFromComment(gson.toJson((new AddCommentToCommentDtoRequest(token2, "Владимирский централ - Круг", "Такого комментария не существует", "Крутая песня"))));
        String errorJson2 = server.deleteCommentFromComment(gson.toJson((new AddCommentToCommentDtoRequest(token2, "Владимирский централ - Круг", "Действительно, отличная песня!", "Такого комментария не существует"))));

        assertEquals(errorJson1, "\"Comment not found\"");
        assertEquals(errorJson2, "\"Previous comment not found\"");
        server.stopServer(null);
    }
}
