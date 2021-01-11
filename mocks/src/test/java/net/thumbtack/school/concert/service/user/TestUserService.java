package net.thumbtack.school.concert.service.user;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.user.UserErrorCode;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.daoimpl.UserDaoimpl;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.dto.request.song.AddCommentToCommentDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddCommentToSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddSongDtoRequest;
import net.thumbtack.school.concert.dto.request.song.RateSongDtoRequest;
import net.thumbtack.school.concert.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.sevice.UserService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestUserService {

    @Test
    public void registerUser() throws IOException {
        Server server = new Server();
        server.startServer(null);
        Gson gson = new Gson();

        User user1 = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        User user2 = new User("Vasya", "Romeo", "vasek1337", "SwordFish");
        User user3 = new User("Ilya", "Punya", "IElogine", "SwordFish");
        String stringUser4 = "{\"firstName\":\"UserName\", \"lastName\":\"UserName\",\"login\":\"VaEg\",\"password\":\"SwordFish123\"}";
        String stringUser5 = "{\"firstName\":\"UserName\", \"lastName\":\"UserName\",\"login\":\"DefLogin\",\"password\":\"123\"}";
        User user6 = new User("Иван", "   ", "defLogin", "defPassword");
        User user7 = new User("    ", "Иванов", "defLogin", "defPassword");
        User user8 = new User("Valentin", "Petya", "NormalLogin", "passwordlowercase123");

        String response1 = server.registerUser(gson.toJson(user1));
        String response2 = server.registerUser(gson.toJson(user2));
        String response3 = server.registerUser(gson.toJson(user3));
        String response4 = server.registerUser(stringUser4);
        String response5 = server.registerUser(stringUser5);
        String response6 = server.registerUser(gson.toJson(user6));
        String response7 = server.registerUser(gson.toJson(user7));
        String response8 = server.registerUser(gson.toJson(user8));

        assertAll(
                () -> assertEquals(2, DataBase.getDataBase().getUserSet().size()),
                () -> assertNotEquals(response1, response3),
                () -> assertEquals(response2, "\"Can not register user: vasek1337 login is already taken\""),
                () -> assertEquals(response4, "\"Can not register user: wrong login VaEg\""),
                () -> assertEquals(response5, "\"Can not register user: wrong password\""),
                () -> assertEquals(response6, "\"Can not register user: wrong last name \""),
                () -> assertEquals(response7, "\"Can not register user: wrong first name \""),
                () -> assertEquals(response8, "\"Can not register user: wrong password\"")
        );
        server.stopServer(null);
    }


    @Test
    public void loginLogoutUser() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User firstUser = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        User secondUser = new User("Vasya", "Pupkin", "vasek1321", "SwordFish");
        User thirdUser = new User("Vasya", "Pupkin", "lalal2314", "1234LalaLala");
        User fourthUser = new User("Vasya", "Pupkin", "vasek13341", "MyPassword32");
        User fifthUser = new User("Vasya", "Pupkin", "vasek13321", "RibaMech123");

        String token1 = server.registerUser(gson.toJson(firstUser));
        String token2 = server.registerUser(gson.toJson(secondUser));
        String token3 = server.registerUser(gson.toJson(thirdUser));
        String token4 = server.registerUser(gson.toJson(fourthUser));
        String token5 = server.registerUser(gson.toJson(fifthUser));

        assertEquals(5, DataBase.getDataBase().getLoginByToken().size());

        server.logoutUser(token1);
        String logoutError = server.logoutUser(token1);
        server.logoutUser(token2);
        server.logoutUser(token3);
        server.logoutUser(token4);
        server.logoutUser(token5);

        LoginUserDtoRequest request = new LoginUserDtoRequest("vasek1337", "SwordFish");
        LoginUserDtoRequest request1 = new LoginUserDtoRequest("vasek1321", "SwordFish");
        LoginUserDtoRequest request2 = new LoginUserDtoRequest("vasek13321", "SwordFish");
        LoginUserDtoRequest request3 = new LoginUserDtoRequest("DefaultLogin", "SwordFish");
        LoginUserDtoRequest request4 = new LoginUserDtoRequest("vasek13321", "RibaMech123");
        LoginUserDtoRequest request5 = new LoginUserDtoRequest(null, "normalPassword123");
        LoginUserDtoRequest request6 = new LoginUserDtoRequest("normalLogin", null);

        String response1 = server.loginUser(gson.toJson(request));
        String response2 = server.loginUser(gson.toJson(request1));
        String response3 = server.loginUser(gson.toJson(request2));
        String response4 = server.loginUser(gson.toJson(request3));
        String response5 = server.loginUser(gson.toJson(request4));
        String response6 = server.loginUser(gson.toJson(request5));
        String response7 = server.loginUser(gson.toJson(request6));

        assertAll(
                () -> assertEquals(3, DataBase.getDataBase().getLoginByToken().size()),
                () -> assertNotEquals(token1, response1),
                () -> assertNotEquals(token2, response2),
                () -> assertNotEquals(token5, response5),
                () -> assertNotEquals(token5, response5),
                () -> assertEquals(response3, "\"Wrong password\""),
                () -> assertEquals(response4, "\"Wrong login\""),
                () -> assertEquals(response6, "\"Wrong login\""),
                () -> assertEquals(response7, "\"Wrong password\""),
                () -> assertEquals(logoutError, "\"Invalid token\"")

        );
        server.stopServer(null);
    }

    @Test
    public void removeUser() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String responde = server.registerUser(gson.toJson(user));
        String token = gson.fromJson(responde, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addSong(gson.toJson(new AddSongDtoRequest(token, "Владимир Путин Молодец", "Россия", "Патриот", "Патриот", 150)));
        server.addSong(gson.toJson(new AddSongDtoRequest(token, "FLEX", "AKYLA", "AKYLA", "DJ MEGA_AKYLA", 150)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Владимирский централ - Круг", "Песня крутяк")));
        server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token, "Владимирский централ - Круг", "Действительно крутая песня", "Песня крутяк")));
        server.removeUser(responde);

        assertTrue(DataBase.getDataBase().getSongs().isEmpty());
        assertEquals(0, DataBase.getDataBase().getSongs().size());
        assertEquals(0, DataBase.getDataBase().getUserSet().size());
        assertEquals(0, DataBase.getDataBase().getLoginByToken().size());
        assertEquals(0, DataBase.getDataBase().getPasswordByLogin().size());

        server.stopServer(null);
    }

    @Test
    public void removeUser1() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        User secondUser = new User("Vasya", "Pupkin", "vasek1321", "SwordFish");
        String respond = server.registerUser(gson.toJson(user));
        String respond1 = server.registerUser(gson.toJson(secondUser));
        String token = gson.fromJson(respond, RegisterUserDtoResponse.class).getToken();
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addSong(gson.toJson(new AddSongDtoRequest(token, "Владимир Путин Молодец", "Россия", "Патриот", "Патриот", 150)));
        server.addSong(gson.toJson(new AddSongDtoRequest(token, "FLEX", "AKYLA", "AKYLA", "DJ MEGA_AKYLA", 150)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Владимирский централ - Круг", "Песня крутяк")));
        server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token, "Владимирский централ - Круг", "Действительно крутая песня", "Песня крутяк")));
        server.rateSong(gson.toJson(new RateSongDtoRequest(token1, "Владимирский централ - Круг", 4)));
        server.removeUser(respond);

        assertAll(
                () -> assertEquals(1, DataBase.getDataBase().getSongs().size()),
                () -> assertEquals(1, DataBase.getDataBase().getUserSet().size()),
                () -> assertEquals(1, DataBase.getDataBase().getLoginByToken().size()),
                () -> assertEquals(1, DataBase.getDataBase().getPasswordByLogin().size())
        );
        server.stopServer(null);
    }

    @Test
    public void removeUser2() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        String respond = server.registerUser(gson.toJson(user));
        String token = gson.fromJson(respond, RegisterUserDtoResponse.class).getToken();
        UUID randomToken = UUID.randomUUID();

        assertNotEquals(token, randomToken);

        String respond1 = server.removeUser(gson.toJson(new RegisterUserDtoResponse(randomToken)));
        String error = gson.fromJson(respond1, String.class);

        assertEquals(error, "Invalid token");
        server.stopServer(null);
    }

    @Test
    public void removeUser3() throws IOException {
        Server server = new Server();
        Gson gson = new Gson();
        server.startServer(null);

        User user = new User("Vasya", "Pupkin", "vasek1337", "SwordFish");
        User secondUser = new User("Vasya", "Pupkin", "vasek1321", "SwordFish");
        String respond = server.registerUser(gson.toJson(user));
        String respond1 = server.registerUser(gson.toJson(secondUser));
        String token = gson.fromJson(respond, RegisterUserDtoResponse.class).getToken();
        String token1 = gson.fromJson(respond1, RegisterUserDtoResponse.class).getToken();

        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимирский централ", "Михаил Круг", "Круг", "Круг", 150)));
        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "Владимир Путин Молодец", "Россия", "Патриот", "Патриот", 150)));
        server.addSong(gson.toJson(new AddSongDtoRequest(token1, "FLEX", "AKYLA", "AKYLA", "DJ MEGA_AKYLA", 150)));
        server.rateSong(gson.toJson(new RateSongDtoRequest(token, "Владимирский централ - Круг", 4)));
        server.addCommentToSong(gson.toJson(new AddCommentToSongDtoRequest(token, "Владимирский централ - Круг", "Песня крутяк")));
        server.addCommentToComment(gson.toJson(new AddCommentToCommentDtoRequest(token1, "Владимирский централ - Круг", "Действительно крутая песня", "Песня крутяк")));
        server.removeUser(respond);

        assertAll(
                () -> assertEquals(3, DataBase.getDataBase().getSongs().size()),
                () -> assertEquals(1, DataBase.getDataBase().getUserSet().size()),
                () -> assertEquals(1, DataBase.getDataBase().getLoginByToken().size()),
                () -> assertEquals(1, DataBase.getDataBase().getPasswordByLogin().size())
        );
        server.stopServer(null);
    }

}
