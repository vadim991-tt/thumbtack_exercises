package net.thumbtack.school.spring.endpoint;

import net.thumbtack.school.spring.model.DtoResponse;
import net.thumbtack.school.spring.model.Recording;
import net.thumbtack.school.spring.model.RecordingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CampaignEndpointTest {
    private final RestTemplate template = new RestTemplate();

    @Test
    public void testStartCampaign() {
        String songName = "RandomSongName";
        Recording recording = new Recording(10, "artist", RecordingType.VIDEO, songName, "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, "~/songs/", null);

        DtoResponse dtoResponse = template.postForObject("http://localhost:8080/api/campaigns/", recording, DtoResponse.class);
        String expectedDtoMessage = String.format("Campaign for %s was started successfully", songName);

        assert dtoResponse != null;
        assertEquals(expectedDtoMessage, dtoResponse.getMessage());
    }

    @Test
    public void testStopCampaign() {
        int id = new Random().nextInt(10);
        String url = String.format("http://localhost:8080/api/campaigns/%s/status/stop", id);

//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        RestTemplate template = new RestTemplate(requestFactory);

        ResponseEntity<DtoResponse> response = template.exchange(url, HttpMethod.PUT, null, DtoResponse.class, id);
        DtoResponse dtoResponse = response.getBody();
        String exceptedDtoMessage = String.format("Campaign with %s ID was stopped successfully", id);

        assert dtoResponse != null;
        assertEquals(dtoResponse.getMessage(), exceptedDtoMessage);
    }

    @Test
    public void testDeleteCampaign() {
        int id = new Random().nextInt(10);
        String url = String.format("http://localhost:8080/api/campaigns/%s", id);

        ResponseEntity<DtoResponse> response = template.exchange(url, HttpMethod.DELETE, null, DtoResponse.class, id);
        DtoResponse dtoResponse = response.getBody();
        String exceptedDtoMessage = String.format("Campaign with %s ID was deleted successfully", id);

        assert dtoResponse != null;
        assertEquals(dtoResponse.getMessage(), exceptedDtoMessage);
    }

}