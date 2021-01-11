package net.thumbtack.school.spring.data;

import com.google.gson.Gson;
import net.thumbtack.school.spring.model.Recording;
import net.thumbtack.school.spring.model.RecordingType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

@Component
@Qualifier("videoStorage")
public class VideoStorage implements DataStorage {

    @Override
    public String save(String path) {
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
            Recording recording = gson.fromJson(br, Recording.class);
            LOGGER.info(recording.toString());
            return UUID.randomUUID().toString();
        } catch (IOException exception) {
            return exception.getMessage();
        }
    }

    @Override
    public Recording save(Recording recording) {
        LOGGER.info(recording.toString());
        LOGGER.info("{} uuid is {}", recording.getName(), UUID.randomUUID());
        return recording;
    }

    @Override
    public Recording get(int id) {
        return new Recording(id, "artist", RecordingType.VIDEO, "SongName", "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, "~/songs", null);
    }
}
