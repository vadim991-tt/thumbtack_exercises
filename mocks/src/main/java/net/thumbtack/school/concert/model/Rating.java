package net.thumbtack.school.concert.model;

import java.util.Objects;

public class Rating {
    private String userName;
    private String song;
    private Integer rating;

    public Rating(String userName, String song, Integer rating) {
        this.userName = userName;
        this.song = song;
        this.rating = rating;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSong() {
        return song;
    }

    public String getShortName() {
        return userName + " - " + song;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    // Оценки считаются одинаковыми, когда их авторы и песни равны
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(userName, rating.userName) &&
                Objects.equals(song, rating.song);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, song);
    }
}
