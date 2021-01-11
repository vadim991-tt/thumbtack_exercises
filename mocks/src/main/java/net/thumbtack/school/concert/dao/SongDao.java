package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.dto.request.song.GetSongsByRequestDtoRequest;
import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.ConcertSong;
import net.thumbtack.school.concert.model.Rating;
import net.thumbtack.school.concert.model.Song;

import java.util.ArrayList;
import java.util.Set;

public interface SongDao {

    String getUserNameFromToken(String token) throws UserException;

    void addSong(Song song) throws SongException;

    String addSongs(ArrayList<Song> songs);

    int addRating(Rating rating) throws SongException;

    void removeSong(String song, String token) throws SongException, UserException;

    int removeRating(String song, String token) throws SongException, UserException;

    int changeRating(Rating rating) throws SongException;

    void addCommentToSong(Comment comment) throws SongException, UserException;

    void addCommentToComment(Comment comment) throws SongException, UserException;

    void deleteCommentFromSong(Comment comment) throws SongException, UserException;

    void changeComment(Comment comment) throws SongException, UserException;

    void deleteCommentFromComment(Comment comment) throws SongException, UserException;

    Set<Song> getSongSet(String token) throws UserException;

    Set<Song> getSongsByComposer(String token, String composer) throws UserException;

    Set<Song> getSongsBySongAuthor(String token, String author) throws UserException;

    Set<Song> getSongsBySinger(String token, String singer) throws UserException;

    Set<ConcertSong> getConcertProgram(String token) throws UserException;
}
