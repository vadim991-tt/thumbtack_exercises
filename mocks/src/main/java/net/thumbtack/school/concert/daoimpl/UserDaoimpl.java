package net.thumbtack.school.concert.daoimpl;


import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.concert.model.User;

import java.util.UUID;


public class UserDaoimpl implements UserDao {
    @Override
    public UUID insert(User user) throws UserException {
        return DataBase.getDataBase().insert(user);
    }

    @Override
    public void logoutUser(String token) throws UserException {
        DataBase.getDataBase().logoutUser(token);
    }

    @Override
    public UUID loginUser(LoginUserDtoRequest request) throws UserException {
        return DataBase.getDataBase().loginUser(request.getLogin(), request.getPassword());
    }

    @Override
    public void removeUser(String token) throws UserException, SongException {
        DataBase.getDataBase().removeUser(token);
    }

}
