package net.thumbtack.school.spring.endpoint;

import net.thumbtack.school.spring.model.DtoResponse;
import net.thumbtack.school.spring.model.Recording;
import net.thumbtack.school.spring.model.RecordingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RecordingsEndpointTest {
    private final RestTemplate template = new RestTemplate();

    @Test
    public void testGetSonByID() {
        int id = new Random().nextInt(10);
        Recording recording = template.getForObject(String.format("http://localhost:8080/api/recordings/%s", id), Recording.class);
        assert recording != null;
        assertEquals(id, recording.getId());
    }

    @Test
    public void testPublishRecording() {
        int id = new Random().nextInt(10);
        String url = String.format("http://localhost:8080/api/recordings/%s", id);
        ResponseEntity<DtoResponse> response = template.exchange(url, HttpMethod.PUT, null, DtoResponse.class, id);
        DtoResponse dtoResponse = response.getBody();
        assert dtoResponse != null;
        assertTrue(dtoResponse.getMessage().contains("was published successfully"));

    }

    @Test
    public void testDeleteRecording() {
        int id = new Random().nextInt(10);
        ResponseEntity<DtoResponse> response = template.exchange("http://localhost:8080/api/recordings/10", HttpMethod.DELETE, null, DtoResponse.class, id);
        DtoResponse dtoResponse = response.getBody();
        assert dtoResponse != null;
        assertTrue(dtoResponse.getMessage().contains("Song with 10 ID was deleted successfully"));
    }

    // Save recording to storage
    @Test
    public void testPost() {
        Recording recording = new Recording("artist", RecordingType.VIDEO, "SongName", "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, "~/songs", "~/videos");
        DtoResponse dtoResponse = template.postForObject("http://localhost:8080/api/recordings/", recording, DtoResponse.class);
        assert dtoResponse != null;
        assertTrue(dtoResponse.getMessage().contains("was saved successfully"));
    }

    @Test
    public void testPostWithWrongYear() {
        Recording recording = new Recording("artist", RecordingType.VIDEO, "SongName", "album name", 1808, "https://site.com/album-picture.png",
                "Rock", 300, "~/songs", "~/videos");

        HttpServerErrorException exc = assertThrows(HttpServerErrorException.class, () -> {
            DtoResponse dtoResponse = template.postForObject("http://localhost:8080/api/recordings/", recording, DtoResponse.class);
            fail();
        });

    }

    @Test
    public void testPostWithNullArtist() {
        Recording recording = new Recording(null, RecordingType.VIDEO, "SongName", "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, "~/songs", null);
        HttpServerErrorException exc = assertThrows(HttpServerErrorException.class, () -> {
            DtoResponse dtoResponse = template.postForObject("http://localhost:8080/api/recordings/", recording, DtoResponse.class);
            fail();
        });
    }

    @Test
    public void testPostWithNullSongName() {
        Recording recording = new Recording("artist", RecordingType.VIDEO, null, "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, "~/songs", null);
        HttpServerErrorException exc = assertThrows(HttpServerErrorException.class, () -> {
            DtoResponse dtoResponse = template.postForObject("http://localhost:8080/api/recordings/", recording, DtoResponse.class);
            fail();
        });
    }

    @Test
    public void testPostWithNullGenre() {
        Recording recording = new Recording("artist", RecordingType.VIDEO, "SongName", "album name", 1999, "https://site.com/album-picture.png",
                null, 300, "~/songs", "~/video");
        HttpServerErrorException exc = assertThrows(HttpServerErrorException.class, () -> {
            DtoResponse dtoResponse = template.postForObject("http://localhost:8080/api/recordings/", recording, DtoResponse.class);
            fail();
        });
    }

    @Test
    public void testPostWithNullDuration() {
        Recording recording = new Recording("artist", RecordingType.VIDEO, "SongName", "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 0, "~/songs", "~/video");
        HttpServerErrorException exc = assertThrows(HttpServerErrorException.class, () -> {
            DtoResponse dtoResponse = template.postForObject("http://localhost:8080/api/recordings/", recording, DtoResponse.class);
            fail();
        });
    }

    @Test
    public void testPostWithoutLinks() {
        Recording recording = new Recording("artist", RecordingType.VIDEO, "SongName", "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, null, null);
        HttpServerErrorException exc = assertThrows(HttpServerErrorException.class, () -> {
            DtoResponse dtoResponse = template.postForObject("http://localhost:8080/api/recordings/", recording, DtoResponse.class);
            fail();
        });
    }


}
