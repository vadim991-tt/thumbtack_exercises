package net.thumbtack.school.concert.daoimpl;

import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.ConcertSong;
import net.thumbtack.school.concert.model.Rating;
import net.thumbtack.school.concert.model.Song;

import java.util.ArrayList;
import java.util.Set;

public class SongDaoimpl implements SongDao {

    @Override
    public String getUserNameFromToken(String token) throws UserException {
        return DataBase.getLoginFromToken(token);
    }

    @Override
    public String addSongs(ArrayList<Song> songs) {
        return DataBase.getDataBase().addSongs(songs);
    }

    @Override
    public void addSong(Song song) throws SongException {
        DataBase.getDataBase().addSong(song);
    }

    @Override
    public int addRating(Rating rating) throws SongException {
        return DataBase.getDataBase().addRating(rating);
    }

    @Override
    public void removeSong(String song, String token) throws SongException, UserException {
        DataBase.getDataBase().removeSong(song, token);
    }

    @Override
    public int removeRating(String song, String token) throws SongException, UserException {
        return DataBase.getDataBase().removeRating(song, token);
    }

    @Override
    public int changeRating(Rating rating) throws SongException {
        return DataBase.getDataBase().changeRating(rating);
    }

    @Override
    public void addCommentToSong(Comment comment) throws SongException {
        DataBase.getDataBase().addCommentToSong(comment);
    }

    @Override
    public void addCommentToComment(Comment comment) throws SongException {
        DataBase.getDataBase().addCommentToComment(comment);
    }

    @Override
    public void deleteCommentFromSong(Comment comment) throws SongException {
        DataBase.getDataBase().deleteCommentFromSong(comment);
    }

    @Override
    public void deleteCommentFromComment(Comment comment) throws SongException {
        DataBase.getDataBase().deleteCommentFromComment(comment);
    }

    @Override
    public void changeComment(Comment comment) throws SongException {
        DataBase.getDataBase().changeComment(comment);
    }

    @Override
    public Set<Song> getSongSet(String token) throws UserException {
        return DataBase.getDataBase().getSongSet(token);
    }

    @Override
    public Set<Song> getSongsByComposer(String token, String composer) throws UserException {
        return DataBase.getDataBase().getSongsByComposer(token, composer);
    }

    @Override
    public Set<Song> getSongsBySongAuthor(String token, String author) throws UserException {
        return DataBase.getDataBase().getSongsBySongAuthor(token, author);
    }

    @Override
    public Set<Song> getSongsBySinger(String token, String singer) throws UserException {
        return DataBase.getDataBase().getSongsBySinger(token, singer);
    }

    @Override
    public Set<ConcertSong> getConcertProgram(String token) throws UserException {
        return DataBase.getDataBase().getConcertProgram(token);
    }
}
