package net.thumbtack.school.concert.server;

import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.sevice.SongService;
import net.thumbtack.school.concert.sevice.UserService;

import java.io.*;


public class Server {
    private static UserService userService;
    private static SongService songService;

    public void startServer(String savedDataFileName) throws IOException {
        if (savedDataFileName == null) {
            DataBase.getDataBase();
        } else {
            DataBase.getDataBase().deserializeDataBaseFromFile(savedDataFileName);
        }
        userService = new UserService();
        songService = new SongService();
    }

    public void stopServer(String savedDataFileName) throws IOException {
        DataBase.getDataBase().serializeDataBaseToFile(savedDataFileName);
        userService = null;
        songService = null;
        DataBase.getDataBase().clear();
    }

    public String registerUser(String requestJsonString) {
        return userService.registerUser(requestJsonString);
    }

    public String logoutUser(String requestJsonString) {
        return userService.logoutUser(requestJsonString);
    }

    public String loginUser(String requestJsonString) {
        return userService.loginUser(requestJsonString);
    }

    public String removeUser(String requestJsonString) {
        return userService.removeUser(requestJsonString);
    }

    public String addSong(String requestJsonString) {
        return songService.addSong(requestJsonString);
    }

    public String addSongs(String requestJsonString) {
        return songService.addSongs(requestJsonString);
    }

    public String removeSong(String requestJsonString) {
        return songService.removeSong(requestJsonString);
    }

    public String rateSong(String requestJsonString) {
        return songService.rateSong(requestJsonString);
    }

    public String removeRating(String requestJsonString) {
        return songService.removeRating(requestJsonString);
    }

    public String changeRating(String requestJsonString) {
        return songService.changeRating(requestJsonString);
    }

    public String addCommentToSong(String requestJsonString) {
        return songService.addCommentToSong(requestJsonString);
    }

    public String addCommentToComment(String requestJsonString) {
        return songService.addCommentToComment(requestJsonString);
    }

    public String deleteCommentFromSong(String requestJsonString) {
        return songService.deleteCommentFromSong(requestJsonString);
    }

    public String deleteCommentFromComment(String requestJsonString) {
        return songService.deleteCommentFromComment(requestJsonString);
    }

    public String changeComment(String requestJsonString) {
        return songService.changeComment(requestJsonString);
    }

    public String getSongSet(String requestJsonString) {
        return songService.getSongSet(requestJsonString);
    }

    public String getSongsByComposer(String requestJsonString) {
        return songService.getSongsByComposer(requestJsonString);
    }

    public String getSongsBySongAuthor(String requestJsonString) {
        return songService.getSongsBySongAuthor(requestJsonString);
    }

    public String getSongsBySinger(String requestJsonString) {
        return songService.getSongsBySinger(requestJsonString);
    }

    public String getConcertProgram(String requestJsonString) {
        return songService.getConcertProgram(requestJsonString);
    }

}
