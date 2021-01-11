package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.concert.model.User;

import java.util.UUID;

public interface UserDao {

    UUID insert(User user) throws UserException;

    void logoutUser(String token) throws UserException;

    UUID loginUser(LoginUserDtoRequest request) throws UserException;

    void removeUser(String token) throws UserException, SongException;
}
