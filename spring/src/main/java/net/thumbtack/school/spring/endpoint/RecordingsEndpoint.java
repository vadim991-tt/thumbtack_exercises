package net.thumbtack.school.spring.endpoint;

import net.thumbtack.school.spring.model.DtoResponse;
import net.thumbtack.school.spring.model.Recording;
import net.thumbtack.school.spring.model.RecordingType;
import net.thumbtack.school.spring.music.ItunesMusicChannel;
import net.thumbtack.school.spring.music.YandexMusicChannel;
import net.thumbtack.school.spring.music.YoutubeMusicChannel;
import net.thumbtack.school.spring.service.RecordingDataHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/recordings")
public class RecordingsEndpoint {
    private final RecordingDataHub recordingDataHub;
    private final ItunesMusicChannel itunesMusicChannel;
    private final YandexMusicChannel yandexMusicChannel;
    private final YoutubeMusicChannel youtubeMusicChannel;

    @Autowired
    public RecordingsEndpoint(RecordingDataHub recordingDataHub, ItunesMusicChannel itunes, YandexMusicChannel yandex, YoutubeMusicChannel youtube) {
        this.recordingDataHub = recordingDataHub;
        itunesMusicChannel = itunes;
        yandexMusicChannel = yandex;
        youtubeMusicChannel = youtube;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public Recording getSong(@PathVariable("id") int id) {
        return recordingDataHub.getSong(id);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DtoResponse saveRecordingToStorage(@RequestBody @Valid Recording recording) {
        if (recording.getType() == RecordingType.AUDIO) {
            recordingDataHub.saveSong(recording);
        } else {
            recordingDataHub.saveVideo(recording);
        }
        return new DtoResponse(recording.getName() + " was saved successfully");
    }


    @RequestMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    public DtoResponse publishRecording(@PathVariable("id") int id) {
        Recording recording = recordingDataHub.getSong(id);
        itunesMusicChannel.publish(recording, ZonedDateTime.now());
        yandexMusicChannel.publish(recording, ZonedDateTime.now());
        youtubeMusicChannel.publish(recording, ZonedDateTime.now());
        return new DtoResponse(recording.getName() + " was published successfully");
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}", method = RequestMethod.DELETE)
    public DtoResponse deleteRecording(@PathVariable("id") int id) {
        itunesMusicChannel.delete(id);
        yandexMusicChannel.delete(id);
        youtubeMusicChannel.delete(id);
        return new DtoResponse(String.format("Song with %s ID was deleted successfully", id));
    }

}
