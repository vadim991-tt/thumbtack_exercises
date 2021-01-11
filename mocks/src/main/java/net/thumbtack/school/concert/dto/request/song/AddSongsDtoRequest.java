package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.base.song.SongErrorCode;
import net.thumbtack.school.concert.base.song.SongException;

public class AddSongsDtoRequest {
    private AddSongDtoRequest[] requests;

    public AddSongsDtoRequest(AddSongDtoRequest[] requests) {
        this.requests = requests;
    }

    public void setArray(AddSongDtoRequest[] array) {
        this.requests = requests;
    }

    public AddSongDtoRequest[] getArray() {
        return requests;
    }

    public static void validate(AddSongsDtoRequest requests) throws SongException {
        if (requests.getArray() == null)
            throw new SongException(SongErrorCode.REQUEST_NOT_FOUND);
    }
}
