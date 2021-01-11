package net.thumbtack.school.concert.dto.response.song;

import org.apache.commons.lang3.StringUtils;

public class AddSongsDtoResponse {
    private String message;

    public AddSongsDtoResponse(String errorString, int songs) {

        int error1 = StringUtils.countMatches(errorString, "already added");
        int error2 = StringUtils.countMatches(errorString, "not found");
        int error3 = StringUtils.countMatches(errorString, "Invalid");
        int error4 = StringUtils.countMatches(errorString, "Wrong");
        int errors = error1 + error2 + error3 + error4;
        int songsAdded = songs - errors;
        if (errors > 0) {
            this.message = String.format("%s out of %s songs added, %s error(s): " + errorString, songsAdded, songs, errors);
        } else {
            this.message = String.format("%s out of %s songs added, 0 error(s).", songsAdded, songs);
        }
    }

    public String getMessage() {
        return message;
    }
}
