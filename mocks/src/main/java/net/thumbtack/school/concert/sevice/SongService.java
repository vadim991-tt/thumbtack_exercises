package net.thumbtack.school.concert.sevice;

import com.google.gson.Gson;
import net.thumbtack.school.concert.base.song.SongException;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.daoimpl.SongDaoimpl;
import net.thumbtack.school.concert.dto.request.song.*;
import net.thumbtack.school.concert.dto.response.song.*;
import net.thumbtack.school.concert.dto.response.user.RegisterUserDtoResponse;
import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.ConcertSong;
import net.thumbtack.school.concert.model.Rating;
import net.thumbtack.school.concert.model.Song;

import java.util.ArrayList;
import java.util.Set;


public class SongService {
    private SongDao songDao = new SongDaoimpl();
    private static final Gson GSON = new Gson();

    public void setSongDao(SongDao songDao) {
        this.songDao = songDao;
    }

    public String addSong(String requestJsonString) {
        AddSongDtoRequest request = GSON.fromJson(requestJsonString, AddSongDtoRequest.class);
        try {
            AddSongDtoRequest.validate(request);
            Song song = AddSongDtoRequest.createSongFromDtoRequest(request);
            song.setRequestAuthor(songDao.getUserNameFromToken(request.getToken()));
            songDao.addSong(song);
            return GSON.toJson(new AddSongDtoResponse(song));
        } catch (SongException | UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String addSongs(String requestJsonString) {
        AddSongsDtoRequest request = GSON.fromJson(requestJsonString, AddSongsDtoRequest.class);
        try {
            AddSongsDtoRequest.validate(request);
            ArrayList<Song> songs = new ArrayList<>();
            for (AddSongDtoRequest elem : request.getArray()) {
                AddSongDtoRequest.validate(elem);
                Song song = AddSongDtoRequest.createSongFromDtoRequest(elem);
                song.setRequestAuthor(songDao.getUserNameFromToken(elem.getToken()));
                songs.add(song);
            }
            String responseStr = songDao.addSongs(songs);
            AddSongsDtoResponse response = new AddSongsDtoResponse(responseStr, request.getArray().length);
            return GSON.toJson(response);
        } catch (SongException | UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String rateSong(String requestJsonString) {
        RateSongDtoRequest request = GSON.fromJson(requestJsonString, RateSongDtoRequest.class);
        try {
            RateSongDtoRequest.validate(request);
            Rating rating = RateSongDtoRequest.createRatingFromDTO(request);
            rating.setUserName(songDao.getUserNameFromToken(request.getToken()));
            int ratingValue = songDao.addRating(rating);
            RateSongDtoResponse response = new RateSongDtoResponse(ratingValue);
            return GSON.toJson(response);
        } catch (SongException | UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String removeSong(String requestJsonString) {
        RemoveSongDtoRequest request = GSON.fromJson(requestJsonString, RemoveSongDtoRequest.class);
        try {
            RemoveSongDtoRequest.validate(request);
            songDao.removeSong(request.getSong(), request.getToken());
            return GSON.toJson(null);
        } catch (SongException | UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String removeRating(String requestJsonString) {
        RemoveRatingDtoRequest request = GSON.fromJson(requestJsonString, RemoveRatingDtoRequest.class);
        try {
            RemoveRatingDtoRequest.validate(request);
            int ratingValue = songDao.removeRating(request.getSong(), request.getToken());
            RateSongDtoResponse response = new RateSongDtoResponse(ratingValue);
            return GSON.toJson(response);
        } catch (SongException | UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String changeRating(String requestJsonString) {
        RateSongDtoRequest request = GSON.fromJson(requestJsonString, RateSongDtoRequest.class);
        try {
            RateSongDtoRequest.validate(request);
            Rating rating = RateSongDtoRequest.createRatingFromDTO(request);
            rating.setUserName(songDao.getUserNameFromToken(request.getToken()));
            int ratingValue = songDao.changeRating(rating);
            RateSongDtoResponse response = new RateSongDtoResponse(ratingValue);
            return GSON.toJson(response);
        } catch (SongException | UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String addCommentToSong(String requestJsonString) {
        AddCommentToSongDtoRequest request = GSON.fromJson(requestJsonString, AddCommentToSongDtoRequest.class);
        try {
            AddCommentToSongDtoRequest.validate(request);
            Comment comment = AddCommentToSongDtoRequest.createCommentFromDto(request);
            comment.setAuthor(songDao.getUserNameFromToken(request.getToken()));
            songDao.addCommentToSong(comment);
            return GSON.toJson(null);
        } catch (UserException | SongException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String addCommentToComment(String requestJsonString) {
        AddCommentToCommentDtoRequest request = GSON.fromJson(requestJsonString, AddCommentToCommentDtoRequest.class);
        try {
            AddCommentToCommentDtoRequest.validate(request);
            Comment comment = AddCommentToCommentDtoRequest.createCommentFromDto(request);
            comment.setAuthor(songDao.getUserNameFromToken(request.getToken()));
            songDao.addCommentToComment(comment);
            return GSON.toJson(null);
        } catch (UserException | SongException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String deleteCommentFromSong(String requestJsonString) {
        AddCommentToSongDtoRequest request = GSON.fromJson(requestJsonString, AddCommentToSongDtoRequest.class);
        try {
            AddCommentToSongDtoRequest.validate(request);
            Comment comment = AddCommentToSongDtoRequest.createCommentFromDto(request);
            comment.setAuthor(songDao.getUserNameFromToken(request.getToken()));
            songDao.deleteCommentFromSong(comment);
            return GSON.toJson(null);
        } catch (UserException | SongException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String changeComment(String requestJsonString) {
        ChangeCommentDtoRequest request = GSON.fromJson(requestJsonString, ChangeCommentDtoRequest.class);
        try {
            ChangeCommentDtoRequest.validate(request);
            Comment comment = ChangeCommentDtoRequest.createCommentFromDto(request);
            comment.setAuthor(songDao.getUserNameFromToken(request.getToken()));
            songDao.changeComment(comment);
            return GSON.toJson(null);
        } catch (UserException | SongException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String deleteCommentFromComment(String requestJsonString) {
        AddCommentToCommentDtoRequest request = GSON.fromJson(requestJsonString, AddCommentToCommentDtoRequest.class);
        try {
            AddCommentToCommentDtoRequest.validate(request);
            Comment comment = AddCommentToCommentDtoRequest.createCommentFromDto(request);
            comment.setAuthor(songDao.getUserNameFromToken(request.getToken()));
            songDao.deleteCommentFromComment(comment);
            return GSON.toJson(null);
        } catch (UserException | SongException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String getSongSet(String requestJsonString) {
        String token = GSON.fromJson(requestJsonString, RegisterUserDtoResponse.class).getToken();
        try {
            Set<Song> songs = songDao.getSongSet(token);
            GetSongsDtoResponse response = new GetSongsDtoResponse(songs);
            return GSON.toJson(response);
        } catch (UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String getSongsByComposer(String requestJsonString) {
        GetSongsByRequestDtoRequest request = GSON.fromJson(requestJsonString, GetSongsByRequestDtoRequest.class);
        try {
            GetSongsByRequestDtoRequest.validate(request);
            Set<Song> songs = songDao.getSongsByComposer(request.getToken(), request.getRequestString());
            GetSongsDtoResponse response = new GetSongsDtoResponse(songs);
            return GSON.toJson(response);
        } catch (UserException | SongException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String getSongsBySongAuthor(String requestJsonString) {
        GetSongsByRequestDtoRequest request = GSON.fromJson(requestJsonString, GetSongsByRequestDtoRequest.class);
        try {
            GetSongsByRequestDtoRequest.validate(request);
            Set<Song> songs = songDao.getSongsBySongAuthor(request.getToken(), request.getRequestString());
            GetSongsDtoResponse response = new GetSongsDtoResponse(songs);
            return GSON.toJson(response);
        } catch (UserException | SongException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String getSongsBySinger(String requestJsonString) {
        GetSongsByRequestDtoRequest request = GSON.fromJson(requestJsonString, GetSongsByRequestDtoRequest.class);
        try {
            GetSongsByRequestDtoRequest.validate(request);
            Set<Song> songs = songDao.getSongsBySinger(request.getToken(), request.getRequestString());
            GetSongsDtoResponse response = new GetSongsDtoResponse(songs);
            return GSON.toJson(response);
        } catch (UserException | SongException e) {
            return GSON.toJson(e.getMessage());
        }
    }

    public String getConcertProgram(String requestJsonString) {
        String token = GSON.fromJson(requestJsonString, RegisterUserDtoResponse.class).getToken();
        try {
            Set<ConcertSong> songs = songDao.getConcertProgram(token);
            GetConcertProgramDtoResponse response = new GetConcertProgramDtoResponse(songs);
            return GSON.toJson(response);
        } catch (UserException e) {
            return GSON.toJson(e.getMessage());
        }
    }
}

