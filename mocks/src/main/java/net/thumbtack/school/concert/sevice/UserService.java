package net.thumbtack.school.concert.sevice;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.daoimpl.UserDaoimpl;

import net.thumbtack.school.concert.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.concert.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.concert.dto.response.user.LoginUserDtoResponse;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.User;

import java.util.UUID;

public class UserService {
    private final Gson GSON = new Gson();
    private UserDao userDao = new UserDaoimpl();

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String registerUser(String requestJsonString) {
        RegisterUserDtoRequest registerUserDtoRequest = GSON.fromJson(requestJsonString, RegisterUserDtoRequest.class);
        try {
            RegisterUserDtoRequest.validate(registerUserDtoRequest);
            User user = RegisterUserDtoRequest.createUserFromDtoRequest(registerUserDtoRequest);
            UUID token = userDao.insert(user);
            RegisterUserDtoResponse response = new RegisterUserDtoResponse(token);
            return GSON.toJson(response);
        } catch (UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String logoutUser(String requestJsonString) {
        String token = GSON.fromJson(requestJsonString, RegisterUserDtoResponse.class).getToken();
        try {
            userDao.logoutUser(token);
            return GSON.toJson(null);
        } catch (UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String loginUser(String requestJsonString) {
        LoginUserDtoRequest request = GSON.fromJson(requestJsonString, LoginUserDtoRequest.class);
        try {
            LoginUserDtoRequest.validate(request);
            UUID token = userDao.loginUser(request);
            LoginUserDtoResponse response = new LoginUserDtoResponse(token);
            return GSON.toJson(response);
        } catch (UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String removeUser(String requestJsonString) {
        String token = GSON.fromJson(requestJsonString, RegisterUserDtoResponse.class).getToken();
        try {
            userDao.removeUser(token);
            return GSON.toJson(null);
        } catch (SongException | UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }
}
