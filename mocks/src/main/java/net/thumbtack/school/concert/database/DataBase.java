package net.thumbtack.school.concert.database;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserErrorCode;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.model.*;


import java.io.*;
import java.util.*;

public class DataBase implements Serializable {
    private static DataBase dataBase;
    private Set<User> userSet;
    private Map<String, String> passwordByLogin;
    private Map<String, String> loginByToken;
    private Set<Song> songs;
    private Map<String, Rating> ratingsByShortName;
    private Map<String, Song> songsByFullName;
    private Map<String, Map<String, Song>> songsByComposer;
    private Map<String, Map<String, Song>> songsBySongAuthor;
    private Map<String, Map<String, Song>> songsBySinger;
    private Map<String, Map<String, Song>> songsByRequester;
    private Map<String, Set<Comment>> commentsByAuthor;


    public DataBase() {
        userSet = new HashSet<>();
        passwordByLogin = new HashMap<>();
        loginByToken = new HashMap<>();
        songs = new HashSet<>();
        songsByFullName = new HashMap<>();
        songsByComposer = new HashMap<>();
        songsBySongAuthor = new HashMap<>();
        songsBySinger = new HashMap<>();
        songsByRequester = new HashMap<>();
        commentsByAuthor = new HashMap<>();
        ratingsByShortName = new HashMap<>();
    }

    public static synchronized DataBase getDataBase() {
        if (dataBase == null) {
            dataBase = new DataBase();
        }
        return dataBase;
    }

    public void dataBaseSetValue(DataBase value) {
        dataBase = value;
    }


    public Set<Song> getSongs() {
        return songs;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public Map<String, String> getPasswordByLogin() {
        return passwordByLogin;
    }

    public Map<String, String> getLoginByToken() {
        return loginByToken;
    }

    public Map<String, Rating> getRatings() {
        return ratingsByShortName;
    }

    public Map<String, Map<String, Song>> getSongsByComposer() {
        return songsByComposer;
    }

    public Map<String, Set<Comment>> getCommByAuthorMap() {
        return commentsByAuthor;
    }

    public Map<String, Map<String, Song>> getSongsBySinger() {
        return songsBySinger;
    }

    public Map<String, Map<String, Song>> getSongsBySongAuthor() {
        return songsBySongAuthor;
    }

    public Map<String, Map<String, Song>> getSongsByRequester() {
        return songsByRequester;
    }

    public UUID insert(User user) throws UserException {
        if (!(userSet.add(user)))
            throw new UserException(net.thumbtack.school.concert.base.user.UserErrorCode.USER_LOGIN_ALREADY_TAKEN, user.getLogin());
        passwordByLogin.put(user.getLogin(), user.getPassword());
        UUID token = UUID.randomUUID();
        loginByToken.put(token.toString(), user.getLogin());
        return token;
    }

    public void logoutUser(String token) throws UserException {
        if (token.isBlank() || loginByToken.remove(token) == null)
            throw new UserException(net.thumbtack.school.concert.base.user.UserErrorCode.INVALID_TOKEN);
    }

    public UUID loginUser(String login, String password) throws UserException {
        String passwordFromDB = passwordByLogin.get(login);
        if (passwordFromDB == null)
            throw new UserException(UserErrorCode.LOGIN_ERROR);
        if (!(passwordFromDB.equals(password))) {
            throw new UserException(UserErrorCode.PASSWORD_ERROR);
        }
        UUID token = UUID.randomUUID();
        loginByToken.put(token.toString(), login);
        return token;
    }

    public void removeUser(String token) throws UserException, SongException {
        String login = getLoginFromToken(token);
        Set<Comment> allComments = new HashSet<>(getCommentsByAuthor(login));
        for (Comment elem : allComments) {
            if (elem.getPreviousComment() == null) {
                deleteCommentFromSong(elem);
            } else {
                deleteCommentFromComment(elem);
            }
        }
        Set<Song> songsByRequester = new HashSet<>(getSongsByRequester(login));
        for (Song elem : songsByRequester) {
            removeSong(elem.getFullSong(), token);
        }
        commentsByAuthor.remove(login);
        userSet.removeIf(elem -> elem.getLogin().equals(login));
        loginByToken.remove(token);
        passwordByLogin.remove(login);
    }

    public void addSong(Song song) throws SongException {
        if (!(songs.add(song)))
            throw new SongException(SongErrorCode.SONG_ALREADY_ADDED, song.getFullSong());
        addSongToCollections(song);
    }

    public String addSongs(ArrayList<Song> songList) {
        StringBuilder sb = new StringBuilder();
        for (Song elem : songList) {
            try {
                addSong(elem);
            } catch (SongException e) {
                sb.append(e.getMessage());
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public void removeSong(String stringSong, String token) throws SongException, UserException {
        Song song = getSongFromFullSongName(stringSong);
        String userName = getLoginFromToken(token);
        if (!(song.getRequestAuthor().equals(userName)))
            throw new SongException(SongErrorCode.REMOVE_SONG_ERROR);
        if (song.getRating() <= 5) {
            songs.remove(song);
            deleteSongFromCollections(song);
        } else {
            song.removeRating(5);
            removeSongFromUserMap(song);
            song.setRequestAuthor("Community");
        }

    }

    public int addRating(Rating rating) throws SongException {
        Song song = getSongFromFullSongName(rating.getSong());
        String userName = rating.getUserName();
        if (song.getRequestAuthor().equals(userName))
            throw new SongException(SongErrorCode.RATING_AUTHOR_ERROR);
        if (ratingsByShortName.containsValue(rating))
            throw new SongException(SongErrorCode.RATING_ERROR);
        ratingsByShortName.put(rating.getShortName(), rating);
        song.addRating(rating.getRating());
        return song.getRating();
    }

    public int changeRating(Rating rating) throws SongException {
        Song song = getSongFromFullSongName(rating.getSong());
        int newRating = rating.getRating();
        String shortName = rating.getShortName();
        Rating ratingFromDB = ratingsByShortName.get(shortName);
        if (ratingFromDB == null)
            throw new SongException(SongErrorCode.REMOVE_RATING_ERROR);
        int prevRating = ratingFromDB.getRating();
        ratingFromDB.setRating(newRating);
        song.removeRating(prevRating);
        song.addRating(newRating);
        return song.getRating();
    }

    public int removeRating(String stringSong, String token) throws SongException, UserException {
        String userName = getLoginFromToken(token);
        Song song = getSongFromFullSongName(stringSong);
        String shortName = userName + " - " + stringSong;
        Rating rating = ratingsByShortName.get(shortName);
        if (rating == null)
            throw new SongException(SongErrorCode.REMOVE_RATING_ERROR);
        int ratingValue = rating.getRating();
        ratingsByShortName.remove(shortName);
        if (song.getRating() - ratingValue <= 0) {
            songs.remove(song);
            deleteSongFromCollections(song);
            return 0;
        } else {
            song.removeRating(ratingValue);
        }
        return song.getRating();
    }

    public void addCommentToSong(Comment comment) throws SongException {
        Song song = getSongFromFullSongName(comment.getSong());
        if (!(song.getComments().add(comment)))
            throw new SongException(SongErrorCode.COMMENT_ERROR);
        addCommentToMap(comment);
    }

    public void addCommentToComment(Comment comment) throws SongException {
        Song song = getSongFromFullSongName(comment.getSong());
        boolean commentHasBeenAdded = false;
        boolean previousCommentExists = false;
        for (Comment elem : song.getComments()) {
            if (comment.getPreviousComment().equals(elem.getText())) {
                commentHasBeenAdded = elem.getComments().add(comment);
                previousCommentExists = true;
            }
        }
        if (!(previousCommentExists))
            throw new SongException(SongErrorCode.WRONG_PREV_COMMENT);
        if (!(commentHasBeenAdded))
            throw new SongException(SongErrorCode.COMMENT_ERROR);
        addCommentToMap(comment);

    }

    public void deleteCommentFromSong(Comment comment) throws SongException {
        Song song = getSongFromFullSongName(comment.getSong());
        boolean deleteComment = false;
        boolean commentExists = false;
        for (Comment elem : song.getComments()) {
            if (comment.getText().equals(elem.getText()) && comment.getAuthor().equals(elem.getAuthor())) {
                commentExists = true;
                if (elem.getComments().isEmpty()) {
                    deleteComment = true;
                } else {
                    deleteCommentFromMap(comment);
                    elem.setAuthor("Community");
                }
            }
        }
        if (!(commentExists))
            throw new SongException(SongErrorCode.WRONG_COMMENT);
        if (deleteComment) {
            song.getComments().remove(comment);
            deleteCommentFromMap(comment);
        }
    }

    public void changeComment(Comment comment) throws SongException {
        Song song = getSongFromFullSongName(comment.getSong());
        String commentToChange = comment.getPreviousComment();
        comment.setPreviousComment(null);
        boolean commentExists = false;
        for (Comment elem : song.getComments()) {
            if (elem.getText().equals(commentToChange) && comment.getAuthor().equals(elem.getAuthor())) {
                commentExists = true;
                deleteCommentFromMap(elem);
                addCommentToMap(comment);
                if (elem.getComments().isEmpty()) {
                    elem.setText(comment.getText());
                } else {
                    elem.setAuthor("Community");
                    song.getComments().add(comment);
                }
            }
        }
        if (!(commentExists))
            throw new SongException(SongErrorCode.WRONG_COMMENT);
    }

    public void deleteCommentFromComment(Comment joinedComment) throws SongException {
        String songName = joinedComment.getSong();
        Song song = getSongFromFullSongName(songName);
        Comment comment = null;
        for (Comment elem : song.getComments()) {
            if (joinedComment.getPreviousComment().equals(elem.getText())) {
                comment = elem;
            }
        }
        if (comment == null)
            throw new SongException(SongErrorCode.WRONG_PREV_COMMENT);
        Set<Comment> joinedComments = comment.getComments();
        if (!(joinedComments.removeIf(elem -> elem.equals(joinedComment))))
            throw new SongException(SongErrorCode.WRONG_COMMENT);
        deleteCommentFromMap(joinedComment);
    }

    public Set<Song> getSongSet(String token) throws UserException {
        getLoginFromToken(token);
        return songs;
    }

    public Set<Song> getSongsByComposer(String token, String composer) throws UserException {
        getLoginFromToken(token);
        Map<String, Song> map = songsByComposer.get(composer);
        if (map == null)
            return new HashSet<>();
        return new HashSet<>(map.values());
    }

    public Set<Song> getSongsBySongAuthor(String token, String songAuthor) throws UserException {
        getLoginFromToken(token);
        Map<String, Song> map = songsBySongAuthor.get(songAuthor);
        if (map == null)
            return new HashSet<>();
        return new HashSet<>(map.values());
    }

    public Set<Song> getSongsBySinger(String token, String singer) throws UserException {
        getLoginFromToken(token);
        Map<String, Song> map = songsBySinger.get(singer);
        if (map == null)
            return new HashSet<>();
        return new HashSet<>(map.values());
    }

    public Set<ConcertSong> getConcertProgram(String token) throws UserException {
        getLoginFromToken(token);
        Comparator<Song> songComparator = new Song.SongRatingComparator().thenComparing(new Song.SongDurationComparator()).thenComparing(new Song.SongNameComparator());
        TreeSet<Song> treeSet = new TreeSet<>(songComparator);
        treeSet.addAll(songs);
        TreeSet<ConcertSong> songsToReturn = new TreeSet<>();
        int concertTimeInSecLeft = 3600;
        int songsAdded = 0;
        int pause = 10;
        int songDuration;
        for (Song elem : treeSet) {
            songDuration = elem.getDuration();
            if (concertTimeInSecLeft - songDuration > 0) {
                concertTimeInSecLeft -= songDuration;
                ConcertSong song = ConcertSong.createConcertSongFromSong(elem);
                songsToReturn.add(song);
                songsAdded++;
                if (songsAdded % 2 == 0 && concertTimeInSecLeft - pause > 0) {
                    concertTimeInSecLeft -= pause;
                }
            }
            if (concertTimeInSecLeft < 60)
                break;
        }
        return songsToReturn;
    }

    public void clear() {
        userSet.clear();
        loginByToken.clear();
        passwordByLogin.clear();
        songs.clear();
        songsByFullName.clear();
        ratingsByShortName.clear();
        songsByComposer.clear();
        songsBySinger.clear();
        songsByRequester.clear();
        songsBySongAuthor.clear();
        commentsByAuthor.clear();
    }

    public void deserializeDataBaseFromFile(String savedDataFileName) throws IOException {
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(savedDataFileName)))) {
            DataBase.getDataBase().dataBaseSetValue(gson.fromJson(br, DataBase.class));
        }
    }

    public void serializeDataBaseToFile(String savedDataFileName) throws IOException {
        if (savedDataFileName != null) {
            Gson gson = new Gson();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(savedDataFileName)))) {
                gson.toJson(DataBase.getDataBase(), bw);
            }
        }
    }

    // Static helpers
    public static Set<Comment> getCommentsByAuthor(String author) {
        Set<Comment> set = DataBase.getDataBase().commentsByAuthor.get(author);
        if (set == null)
            return new HashSet<>();
        return set;

    }

    public static Set<Song> getSongsByRequester(String userName) {
        Map<String, Song> songs = DataBase.getDataBase().songsByRequester.get(userName);
        if (songs == null)
            return new HashSet<>();
        return new HashSet<>(songs.values());
    }

    public static Song getSongFromFullSongName(String songName) throws SongException {
        Song song = DataBase.getDataBase().songsByFullName.get(songName);
        if (song == null)
            throw new SongException(SongErrorCode.SONG_NOT_FOUND);
        return song;
    }

    public static String getLoginFromToken(String token) throws UserException {
        String userName = DataBase.getDataBase().getLoginByToken().get(token);
        if (userName == null)
            throw new UserException(UserErrorCode.INVALID_TOKEN);
        return userName;
    }

    public static void addSongToMap(Song song, String request, Map<String, Map<String, Song>> map) {
        Map<String, Song> songsBySmth = map.get(request);
        if (songsBySmth == null) {
            Map<String, Song> songs = new HashMap<>();
            songs.put(song.getFullSong(), song);
            map.put(request, songs);
        } else {
            songsBySmth.put(song.getFullSong(), song);
        }
    }

    public static void addCommentToMap(Comment comment) {
        String userName = comment.getAuthor();
        Set<Comment> comments = DataBase.getDataBase().commentsByAuthor.get(userName);
        if (comments == null) {
            Set<Comment> newComments = new HashSet<>();
            newComments.add(comment);
            DataBase.getDataBase().commentsByAuthor.put(comment.getAuthor(), newComments);
        } else {
            comments.add(comment);
        }
    }

    public static void deleteCommentFromMap(Comment comment) {
        String userName = comment.getAuthor();
        Set<Comment> comments = DataBase.getDataBase().commentsByAuthor.get(userName);
        comments.remove(comment);
    }

    public static void removeSongFromMap(Song song, String request, Map<String, Map<String, Song>> map) {
        Map<String, Song> songsBySmth = map.get(request);
        songsBySmth.remove(song.getFullSong());
        if (songsBySmth.size() == 0)
            map.remove(request);
    }

    public static void removeSongFromUserMap(Song song) {
        String user = song.getRequestAuthor();
        Map<String, Song> songs = DataBase.getDataBase().getSongsByRequester().get(user);
        songs.remove(song.getFullSong());
        if (songs.size() == 0)
            DataBase.getDataBase().getSongsByRequester().remove(user);
    }

    public static void addSongToCollections(Song song) {
        DataBase.getDataBase().songsByFullName.put(song.getFullSong(), song);
        addSongToMap(song, song.getComposer(), DataBase.getDataBase().songsByComposer);
        addSongToMap(song, song.getSongAuthor(), DataBase.getDataBase().songsBySongAuthor);
        addSongToMap(song, song.getSinger(), DataBase.getDataBase().songsBySinger);
        addSongToMap(song, song.getRequestAuthor(), DataBase.getDataBase().songsByRequester);
    }

    public static void deleteSongFromCollections(Song song) {
        DataBase.getDataBase().songsByFullName.remove(song.getFullSong());
        removeSongFromMap(song, song.getComposer(), DataBase.getDataBase().songsByComposer);
        removeSongFromMap(song, song.getSongAuthor(), DataBase.getDataBase().songsBySongAuthor);
        removeSongFromMap(song, song.getSinger(), DataBase.getDataBase().songsBySinger);
        if (!(song.getRequestAuthor().equals("Community"))) {
            removeSongFromMap(song, song.getRequestAuthor(), DataBase.getDataBase().songsByRequester);
        }
    }
}





