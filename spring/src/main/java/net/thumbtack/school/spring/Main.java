package net.thumbtack.school.spring;

import com.google.gson.Gson;
import net.thumbtack.school.spring.model.Recording;
import net.thumbtack.school.spring.model.RecordingType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Recording recording = new Recording(5, "artist", RecordingType.AUDIO, "name", "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, "json_songs/some_audio.txt", null);
        Gson gson = new Gson();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("json_songs/some_audio.txt")))) {
            gson.toJson(recording, bw);
        }

        Recording recording1 = new Recording(6, "artist", RecordingType.VIDEO, "name", "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, null, "json_songs/some_video.txt");
        ;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("json_songs/some_video.txt")))) {
            gson.toJson(recording1, bw);
        }

    }
}
