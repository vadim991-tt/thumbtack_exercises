package net.thumbtack.school.mock.bot;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.dto.response.song.GetSongsDtoResponse;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;


public class Bot {

    public static String getListSize(String jsonRequest, Server server) throws SongException {
        Gson gson = new Gson();

        BotDtoRequest dtoRequest = gson.fromJson(jsonRequest, BotDtoRequest.class);
        String stringRequest = dtoRequest.getRequest();
        User user = new User("Bot", "Botov", "BotBotov999", "botPassword999");

        String token;
        int size;

        if (stringRequest.equals("All songs")) {
            token = server.registerUser(gson.toJson(user));
            String jsonResp = server.getSongSet(token);
            size = gson.fromJson(jsonResp, GetSongsDtoResponse.class).getSongs().size();
        } else if (stringRequest.equals("Concert program songs")) {
            token = server.registerUser(gson.toJson(user));
            String jsonProgram = server.getConcertProgram(token);
            size = gson.fromJson(jsonProgram, GetSongsDtoResponse.class).getSongs().size();
        } else {
            throw new SongException(SongErrorCode.REQUEST_NOT_FOUND);
        }

        server.removeUser(token);
        return gson.toJson(new BotDtoResponse(size));
    }

}
