package net.thumbtack.school.concert.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Song {
    private String songName;
    private String composer;
    private String songAuthor;
    private String singer;
    private String requestAuthor;
    private int duration;
    private Integer rating;
    private Integer rates;
    private Set<Comment> comments;


    public Song(String songName, String composer, String songAuthor, String singer, String requestAuthor, int duration) {
        this.songName = songName;
        this.composer = composer;
        this.songAuthor = songAuthor;
        this.singer = singer;
        this.requestAuthor = requestAuthor;
        this.duration = duration;
        this.rating = 5;
        this.rates = 1;
        this.comments = new HashSet<>();
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongName() {
        return songName;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getComposer() {
        return composer;
    }

    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
    }

    public String getSongAuthor() {
        return songAuthor;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSinger() {
        return singer;
    }

    public void setRequestAuthor(String requestAuthor) {
        this.requestAuthor = requestAuthor;
    }

    public String getRequestAuthor() {
        return requestAuthor;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Integer getRates() {
        return rates;
    }

    public String getFullSong() {
        return songName + " - " + singer;
    }

    public void addRating(Integer rating) {
        this.rating += rating;
        rates++;
    }

    public void removeRating(Integer rating) {
        this.rating -= rating;
        rates--;
    }

    // Песни считаются одинаковыми когда их названия и исполнители равны
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(songName, song.songName) &&
                Objects.equals(singer, song.singer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songName, singer);
    }

    public static class SongRatingComparator implements Comparator<Song> {

        @Override
        public int compare(Song o1, Song o2) {
            if (o1.getRating() > o2.getRating())
                return -1;
            if (o1.getRating() < o2.getRating())
                return 1;
            return 0;
        }
    }

    public static class SongDurationComparator implements Comparator<Song> {

        @Override
        public int compare(Song o1, Song o2) {
            if (o1.getDuration() > o2.getDuration())
                return 1;
            if (o1.getDuration() < o2.getDuration())
                return -1;
            return 0;
        }
    }

    public static class SongNameComparator implements Comparator<Song> {

        @Override
        public int compare(Song o1, Song o2) {
            return o1.getFullSong().compareTo(o2.getFullSong());
        }
    }
}
