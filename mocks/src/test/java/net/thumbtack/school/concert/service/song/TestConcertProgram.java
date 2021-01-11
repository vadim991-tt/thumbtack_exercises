package net.thumbtack.school.concert.service.song;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.dto.request.song.AddCommentToSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddSongsDtoRequest;
import net.thumbtack.school.concert.dto.request.song.RateSongDtoRequest;
import net.thumbtack.school.concert.dto.response.song.GetConcertProgramDtoResponse;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.ConcertSong;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestConcertProgram {
    @Test
    public void TestConcertProgram1() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        Random random = new Random();
        server.startServer(null);
        User user1 = new User("User", "User", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Песня1", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Песня2", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Песня3", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "Песня4", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Песня5", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request6 = new AddSongDtoRequest(token, "Песня6", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request7 = new AddSongDtoRequest(token, "Песня7", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request8 = new AddSongDtoRequest(token, "Песня8", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request9 = new AddSongDtoRequest(token, "Песня9", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request10 = new AddSongDtoRequest(token, "Песня10", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request11 = new AddSongDtoRequest(token, "Песня11", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request12 = new AddSongDtoRequest(token, "Песня12", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request13 = new AddSongDtoRequest(token, "Песня13", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request14 = new AddSongDtoRequest(token, "Песня14", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request15 = new AddSongDtoRequest(token, "Песня15", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request16 = new AddSongDtoRequest(token, "Песня16", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request17 = new AddSongDtoRequest(token, "Песня17", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request18 = new AddSongDtoRequest(token, "Песня18", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request19 = new AddSongDtoRequest(token, "Песня19", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request20 = new AddSongDtoRequest(token, "Песня20", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request21 = new AddSongDtoRequest(token, "Песня21", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request22 = new AddSongDtoRequest(token, "Песня22", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request23 = new AddSongDtoRequest(token, "Песня23", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request24 = new AddSongDtoRequest(token, "Песня24", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request25 = new AddSongDtoRequest(token, "Песня25", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request26 = new AddSongDtoRequest(token, "Песня26", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request27 = new AddSongDtoRequest(token, "Песня27", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request28 = new AddSongDtoRequest(token, "Песня28", "Композитор", "Автор", "Группа", 60 + random.nextInt(300));
        AddSongDtoRequest request29 = new AddSongDtoRequest(token, "Песня29", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest request30 = new AddSongDtoRequest(token, "Песня30", "Этой песни", "не будет", "Группа", 300);

        AddSongDtoRequest[] requests = {request1, request2, request3, request4, request5, request6, request7, request8, request9, request10, request11, request12, request13, request14, request15, request16};
        server.addSongs(gson.toJson(new AddSongsDtoRequest(requests)));
        AddSongDtoRequest[] moreRequests = {request17, request18, request19, request20, request21, request22, request23, request24, request25, request26, request27, request28, request29, request30};
        server.addSongs(gson.toJson(new AddSongsDtoRequest(moreRequests)));
        assertEquals(DataBase.getDataBase().getSongs().size(), 30);
        User user2 = new User("User", "User", "Login123450", "SwordFish");
        String token2 = gson.fromJson(server.registerUser(gson.toJson(user2)), RegisterUserDtoResponse.class).getToken();
        User user3 = new User("User", "User", "Login123451", "SwordFish");
        String token3 = gson.fromJson(server.registerUser(gson.toJson(user3)), RegisterUserDtoResponse.class).getToken();
        User user4 = new User("User", "User", "Login123452", "SwordFish");
        String token4 = gson.fromJson(server.registerUser(gson.toJson(user4)), RegisterUserDtoResponse.class).getToken();
        User user5 = new User("User", "User", "Login123453", "SwordFish");
        String token5 = gson.fromJson(server.registerUser(gson.toJson(user5)), RegisterUserDtoResponse.class).getToken();
        User user6 = new User("User", "User", "Login123454", "SwordFish");
        String token6 = gson.fromJson(server.registerUser(gson.toJson(user6)), RegisterUserDtoResponse.class).getToken();
        String[] notBiasedUsers = {token2, token3, token4, token5, token6};
        for (String elem : notBiasedUsers) {
            RateSongDtoRequest rateRequest1 = new RateSongDtoRequest(elem, "Песня1 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest2 = new RateSongDtoRequest(elem, "Песня2 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest3 = new RateSongDtoRequest(elem, "Песня3 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest4 = new RateSongDtoRequest(elem, "Песня4 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest5 = new RateSongDtoRequest(elem, "Песня5 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest6 = new RateSongDtoRequest(elem, "Песня6 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest7 = new RateSongDtoRequest(elem, "Песня7 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest8 = new RateSongDtoRequest(elem, "Песня8 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest9 = new RateSongDtoRequest(elem, "Песня9 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest10 = new RateSongDtoRequest(elem, "Песня10 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest11 = new RateSongDtoRequest(elem, "Песня11 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest12 = new RateSongDtoRequest(elem, "Песня12 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest13 = new RateSongDtoRequest(elem, "Песня13 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest14 = new RateSongDtoRequest(elem, "Песня14 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest15 = new RateSongDtoRequest(elem, "Песня15 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest16 = new RateSongDtoRequest(elem, "Песня16 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest17 = new RateSongDtoRequest(elem, "Песня17 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest18 = new RateSongDtoRequest(elem, "Песня18 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest19 = new RateSongDtoRequest(elem, "Песня19 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest20 = new RateSongDtoRequest(elem, "Песня20 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest21 = new RateSongDtoRequest(elem, "Песня21 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest22 = new RateSongDtoRequest(elem, "Песня22 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest23 = new RateSongDtoRequest(elem, "Песня23 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest24 = new RateSongDtoRequest(elem, "Песня24 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest25 = new RateSongDtoRequest(elem, "Песня25 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest26 = new RateSongDtoRequest(elem, "Песня26 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest27 = new RateSongDtoRequest(elem, "Песня27 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest rateRequest28 = new RateSongDtoRequest(elem, "Песня28 - Группа", 1 + random.nextInt(5));
            RateSongDtoRequest[] rateArray1 = {rateRequest1, rateRequest2, rateRequest3, rateRequest3, rateRequest4, rateRequest5, rateRequest6, rateRequest7, rateRequest8, rateRequest9, rateRequest10, rateRequest11, rateRequest12, rateRequest13, rateRequest14};
            for (RateSongDtoRequest rateElem : rateArray1)
                server.rateSong(gson.toJson(rateElem));
            RateSongDtoRequest[] rateArray2 = {rateRequest15, rateRequest16, rateRequest17, rateRequest18, rateRequest19, rateRequest20, rateRequest21, rateRequest22, rateRequest23, rateRequest24, rateRequest25, rateRequest26, rateRequest27, rateRequest28};
            for (RateSongDtoRequest rateElem : rateArray2)
                server.rateSong(gson.toJson(rateElem));
        }
        assertEquals(DataBase.getDataBase().getRatings().size(), 140);
        String json = server.getConcertProgram(gson.toJson(response));
        Set<ConcertSong> program = gson.fromJson(json, GetConcertProgramDtoResponse.class).getSongs();
        for (ConcertSong elem : program)
            if ((elem.getFullSong().equals("Песня29 - Группа") || (elem.getFullSong().equals("Песня30 - Группа"))))
                fail();
        server.stopServer(null);
    }

    @Test
    public void TestConcertProgram2() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user1 = new User("User", "User", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        String token = response.getToken();

        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Песня1", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Песня2", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Песня3", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "Песня4", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Песня5", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request6 = new AddSongDtoRequest(token, "Песня6", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request7 = new AddSongDtoRequest(token, "Песня7", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request8 = new AddSongDtoRequest(token, "Песня8", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request9 = new AddSongDtoRequest(token, "Песня9", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request10 = new AddSongDtoRequest(token, "Песня10", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request11 = new AddSongDtoRequest(token, "Песня11", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request12 = new AddSongDtoRequest(token, "Песня12", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request13 = new AddSongDtoRequest(token, "Песня13", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request14 = new AddSongDtoRequest(token, "Песня14", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request15 = new AddSongDtoRequest(token, "Песня15", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request16 = new AddSongDtoRequest(token, "Песня16", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request17 = new AddSongDtoRequest(token, "Песня17", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request18 = new AddSongDtoRequest(token, "Песня18", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest request19 = new AddSongDtoRequest(token, "Песня19", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest request20 = new AddSongDtoRequest(token, "Песня20", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest request21 = new AddSongDtoRequest(token, "Песня21", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest request22 = new AddSongDtoRequest(token, "Песня22", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest request23 = new AddSongDtoRequest(token, "Песня23", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest request24 = new AddSongDtoRequest(token, "Песня24", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest request25 = new AddSongDtoRequest(token, "Песня25", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest request26 = new AddSongDtoRequest(token, "Песня26", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request27 = new AddSongDtoRequest(token, "Песня27", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request28 = new AddSongDtoRequest(token, "Песня28", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request29 = new AddSongDtoRequest(token, "Песня29", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request30 = new AddSongDtoRequest(token, "Песня30", "Этой песни", "не будет", "Группа", 300);
        AddSongDtoRequest[] requests = {request1, request2, request3, request4, request5, request6, request7, request8, request9, request10, request11, request12, request13, request14, request15, request16};
        server.addSongs(gson.toJson(new AddSongsDtoRequest(requests)));
        AddSongDtoRequest[] moreRequests = {request17, request18, request19, request20, request21, request22, request23, request24, request25, request26, request27, request28, request29, request30};
        server.addSongs(gson.toJson(new AddSongsDtoRequest(moreRequests)));
        String json = server.getConcertProgram(gson.toJson(response));
        Set<ConcertSong> program = gson.fromJson(json, GetConcertProgramDtoResponse.class).getSongs();
        assertEquals(program.size(), 19);
        for (ConcertSong elem : program)
            if (elem.getSongAuthor().equals("не будет"))
                fail();
        server.stopServer(null);
    }

    @Test
    public void TestConcertProgram3() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);
        User user1 = new User("User", "User", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        String token = response.getToken();
        AddSongDtoRequest request1 = new AddSongDtoRequest(token, "Песня1", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request2 = new AddSongDtoRequest(token, "Песня2", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request3 = new AddSongDtoRequest(token, "Песня3", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request4 = new AddSongDtoRequest(token, "Песня4", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request5 = new AddSongDtoRequest(token, "Песня5", "Композитор", "Автор", "Группа", 100);
        AddSongDtoRequest request6 = new AddSongDtoRequest(token, "Песня6", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request7 = new AddSongDtoRequest(token, "Песня7", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request8 = new AddSongDtoRequest(token, "Песня8", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request9 = new AddSongDtoRequest(token, "Песня9", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request10 = new AddSongDtoRequest(token, "Песня10", "Композитор", "Автор", "Группа", 200);
        AddSongDtoRequest request11 = new AddSongDtoRequest(token, "Песня11", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request12 = new AddSongDtoRequest(token, "Песня12", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request13 = new AddSongDtoRequest(token, "Песня13", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request14 = new AddSongDtoRequest(token, "Песня14", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request15 = new AddSongDtoRequest(token, "Песня15", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest request16 = new AddSongDtoRequest(token, "Песня16", "Композитор", "Автор", "Группа", 300);
        AddSongDtoRequest[] requests = {request1, request2, request3, request4, request5, request6, request7, request8, request9, request10, request11, request12, request13, request14, request15, request16};
        server.addSongs(gson.toJson(new AddSongsDtoRequest(requests)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня1 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня2 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня3 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня4 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня5 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня6 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня7 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня8 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня9 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня10 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня11 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня12 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня13 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня14 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня15 - Группа", "Оставлю тут комментарий чтобы все видели")));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Песня16 - Группа", "Оставлю тут комментарий чтобы все видели")));
        String json = server.getConcertProgram(gson.toJson(response));
        Set<ConcertSong> program = gson.fromJson(json, GetConcertProgramDtoResponse.class).getSongs();
        for (ConcertSong elem : program)
            if (elem.getComments().isEmpty())
                fail();
        server.stopServer(null);
    }

    @Test
    public void TestConcertProgramErrors() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);
        User user1 = new User("User", "User", "vasek1337", "SwordFish");
        String respond1 = server.registerUser(gson.toJson(user1));
        RegisterUserDtoResponse response = gson.fromJson(respond1, RegisterUserDtoResponse.class);
        response.setToken(null);
        String error = server.getConcertProgram(gson.toJson(response));
        assertEquals(error, "\"Invalid token\"");
        server.stopServer(null);
    }
}
