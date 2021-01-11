package net.thumbtack.school.mock;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.dto.request.song.AddSongDtoRequest;
import net.thumbtack.school.concert.dto.response.song.GetSongsDtoResponse;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.mock.bot.Bot;
import net.thumbtack.school.mock.bot.BotDtoRequest;
import net.thumbtack.school.mock.bot.BotDtoResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class TestBotOperations {


    // Simple Junit Test
    @Test
    public void getSongsSizeTest() throws IOException, SongException {
        // Preparing
        Server server = new Server();
        server.startServer(null);
        Gson gson = new Gson();
        Random random = new Random();

        User user = new User("User", "User", "vasek1337", "SwordFish");
        String response = server.registerUser(gson.toJson(user));
        RegisterUserDtoResponse dtoResponse = gson.fromJson(response, RegisterUserDtoResponse.class);
        String token = dtoResponse.getToken();

        int randomSize = random.nextInt(100);

        for (int i = 0; i < randomSize; i++) {
            String songName = String.format("Песня %s", i);
            AddSongDtoRequest request = new AddSongDtoRequest(token, songName, "Композитор", "Автор", "Группа", 99);
            server.addSong(gson.toJson(request));
        }

        // Tests
        String jsonSongSet = server.getSongSet(gson.toJson(new RegisterUserDtoResponse(UUID.fromString(token))));
        Set<Song> songSet = gson.fromJson(jsonSongSet, GetSongsDtoResponse.class).getSongs();

        String botResponse = Bot.getListSize(gson.toJson(new BotDtoRequest("All songs")), server);
        int botSize = gson.fromJson(botResponse, BotDtoResponse.class).getResponse();

        assertEquals(songSet.size(), randomSize, botSize);

        server.stopServer(null);
    }

    // Mock Tests
    @Test
    public void getSongSizeUsingMocks2() throws IOException, SongException {
        // Preparing
        Server server = new Server();
        server.startServer(null);
        Gson gson = new Gson();
        Random random = new Random();

        User user = new User("User", "User", "vasek1337", "SwordFish");
        String response = server.registerUser(gson.toJson(user));
        RegisterUserDtoResponse dtoResponse = gson.fromJson(response, RegisterUserDtoResponse.class);
        String token = dtoResponse.getToken();

        int randomSize = random.nextInt(100);

        for (int i = 0; i < randomSize; i++) {
            String songName = String.format("Песня %s", i);
            AddSongDtoRequest request = new AddSongDtoRequest(token, songName, "Композитор", "Автор", "Группа", 99);
            server.addSong(gson.toJson(request));
        }

        // Bot operations
        Server spyServer = spy(server);
        User bot = new User("Bot", "Botov", "BotBotov999", "botPassword999");
        String json = Bot.getListSize(gson.toJson(new BotDtoRequest("All songs")), spyServer);
        int botSize = gson.fromJson(json, BotDtoResponse.class).getResponse();

        assertEquals(randomSize, botSize);
        verify(spyServer, times(1)).registerUser(gson.toJson(bot));
        verify(spyServer, times(1)).getSongSet(anyString());
        verify(spyServer, times(1)).removeUser(anyString());

        server.stopServer(null);

    }

    @Test
    public void getSongSetError() throws IOException {
        Server server = new Server();
        server.startServer(null);
        Server spyServer = spy(server);
        Gson gson = new Gson();

        try {
            Bot.getListSize(gson.toJson(new BotDtoRequest("WrongRequest")), spyServer);
            fail();
        } catch (SongException ex) {
            // ignored
        }
        verifyZeroInteractions(spyServer);
        spyServer.stopServer(null);
    }


}
