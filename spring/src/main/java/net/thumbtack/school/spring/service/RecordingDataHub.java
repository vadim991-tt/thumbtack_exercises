package net.thumbtack.school.spring.service;

import net.thumbtack.school.spring.data.AudioStorage;
import net.thumbtack.school.spring.data.VideoStorage;
import net.thumbtack.school.spring.model.Recording;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("recordingDataHub")
public class RecordingDataHub {
    private final AudioStorage audioStorage;
    private final VideoStorage videoStorage;

    @Autowired
    public RecordingDataHub(AudioStorage audioStorage, VideoStorage videoStorage) {
        this.audioStorage = audioStorage;
        this.videoStorage = videoStorage;
    }

    public String saveSongFromPath(String path) {
        return audioStorage.save(path);
    }

    public Recording saveSong(Recording recording) {
        return audioStorage.save(recording);
    }

    public Recording saveVideo(Recording recording) {
        return videoStorage.save(recording);
    }

    public String saveVideoFromPath(String path) {
        return videoStorage.save(path);
    }

    public Recording getSong(int id) {
        return audioStorage.get(id);
    }

    public Recording getVideo(int id) {
        return videoStorage.get(id);
    }

}
