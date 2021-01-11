package net.thumbtack.school.concert.model;

import java.util.Set;

public class ConcertSong implements Comparable<ConcertSong> {
    private String songName;
    private String composer;
    private String songAuthor;
    private String singer;
    private String requestAuthor;
    private int duration;
    private double averageRating;
    private Set<Comment> comments;

    public ConcertSong(String songName, String composer, String songAuthor, String singer, String requestAuthor, int duration, double averageRating, Set<Comment> comments) {
        this.songName = songName;
        this.composer = composer;
        this.songAuthor = songAuthor;
        this.singer = singer;
        this.requestAuthor = requestAuthor;
        this.duration = duration;
        this.averageRating = averageRating;
        this.comments = comments;
    }


    public void setAverageRating(int averageRating) {
        this.averageRating = averageRating;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public String getFullSong() {
        return songName + " - " + singer;
    }

    public String getSongAuthor() {
        return songAuthor;
    }

    public static ConcertSong createConcertSongFromSong(Song song) {
        String songName = song.getSongName();
        String composer = song.getComposer();
        String songAuthor = song.getSongAuthor();
        String singer = song.getSinger();
        String requester = song.getRequestAuthor();
        int duration = song.getDuration();
        double averageRating = (double) song.getRating() / song.getRates();
        double normalRating = Math.round(averageRating * 100) / 100.0;
        Set<Comment> comments = song.getComments();
        return new ConcertSong(songName, composer, songAuthor, singer, requester, duration, normalRating, comments);
    }

    @Override
    public int compareTo(ConcertSong o) {
        if (averageRating > o.getAverageRating())
            return -1;
        if (averageRating < o.getAverageRating())
            return 1;
        String fullSongName = songName + " - " + singer;
        return fullSongName.compareTo(o.getFullSong());
    }
}
