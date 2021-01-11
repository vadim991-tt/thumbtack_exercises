package net.thumbtack.school.spring.model;


import net.thumbtack.school.spring.validator.RecordingWithLink;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

@RecordingWithLink
public class Recording {
    @NotNull
    private int id;
    @NotNull
    private String artist;
    private RecordingType type;
    @NotNull
    private String name;
    private String albumName;
    @NotNull
    @DecimalMin("1970")
    private int year; //
    private String albumPictureLink;
    @NotNull
    private String genre;
    @Positive
    private int durationInSec;
    private String linkToAudioFile;
    private String linkToVideoFile;

    public Recording() {

    }

    public Recording(@NotNull int id, @NotNull String artist, RecordingType type, @NotNull String name, String albumName, @NotNull @DecimalMin("1970") int year, String albumPictureLink, @NotNull String genre, @NotNull int durationInSec, String linkToAudioFile, String linkToVideoFile) {
        this.id = id;
        this.artist = artist;
        this.type = type;
        this.name = name;
        this.albumName = albumName;
        this.year = year;
        this.albumPictureLink = albumPictureLink;
        this.genre = genre;
        this.durationInSec = durationInSec;
        this.linkToAudioFile = linkToAudioFile;
        this.linkToVideoFile = linkToVideoFile;
    }

    public Recording(@NotNull String artist, RecordingType type, @NotNull String name, String albumName, @NotNull @DecimalMin("1970") int year, String albumPictureLink, @NotNull String genre, @NotNull int durationInSec, String linkToAudioFile, String linkToVideoFile) {
        this(0, artist, type, name, albumName, year, albumPictureLink, genre, durationInSec, linkToAudioFile, linkToVideoFile);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public RecordingType getType() {
        return type;
    }

    public void setType(RecordingType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAlbumPictureLink() {
        return albumPictureLink;
    }

    public void setAlbumPictureLink(String albumPictureLink) {
        this.albumPictureLink = albumPictureLink;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDurationInSec() {
        return durationInSec;
    }

    public void setDurationInSec(int durationInSec) {
        this.durationInSec = durationInSec;
    }

    public String getLinkToAudioFile() {
        return linkToAudioFile;
    }

    public void setLinkToAudioFile(String linkToAudioFile) {
        this.linkToAudioFile = linkToAudioFile;
    }

    public String getLinkToVideoFile() {
        return linkToVideoFile;
    }

    public void setLinkToVideoFile(String linkToVideoFile) {
        this.linkToVideoFile = linkToVideoFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recording recording = (Recording) o;
        return id == recording.id &&
                year == recording.year &&
                durationInSec == recording.durationInSec &&
                Objects.equals(artist, recording.artist) &&
                type == recording.type &&
                Objects.equals(name, recording.name) &&
                Objects.equals(albumName, recording.albumName) &&
                Objects.equals(albumPictureLink, recording.albumPictureLink) &&
                Objects.equals(genre, recording.genre) &&
                Objects.equals(linkToAudioFile, recording.linkToAudioFile) &&
                Objects.equals(linkToVideoFile, recording.linkToVideoFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artist, type, name, albumName, year, albumPictureLink, genre, durationInSec, linkToAudioFile, linkToVideoFile);
    }
}
