package net.thumbtack.school.spring.endpoint.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thumbtack.school.spring.endpoint.RecordingsEndpoint;
import net.thumbtack.school.spring.model.Recording;
import net.thumbtack.school.spring.model.RecordingType;
import net.thumbtack.school.spring.music.ItunesMusicChannel;
import net.thumbtack.school.spring.music.YandexMusicChannel;
import net.thumbtack.school.spring.music.YoutubeMusicChannel;
import net.thumbtack.school.spring.service.GlobalErrorHandler;
import net.thumbtack.school.spring.service.RecordingDataHub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RecordingsEndpoint.class)
public class MockRecordingsEndpointTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RecordingDataHub recordingDataHub;

    @MockBean
    private ItunesMusicChannel itunesMusicChannel;

    @MockBean
    private YandexMusicChannel yandexMusicChannel;

    @MockBean
    private YoutubeMusicChannel youtubeMusicChannel;

    @Test
    public void testGetSongByID() throws Exception {
        mvc.perform(get("/api/recordings/2")).andExpect(status().isOk());
    }


    @Test
    public void testSaveRecordingToStorage() throws Exception {
        Recording recording = new Recording(0, "artist", RecordingType.VIDEO, "SongName", "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, "~/songs", null);

        MvcResult result = mvc.perform(post("/api/recordings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recording)))
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("was saved successfully"));
    }

    @Test
    public void testSaveRecordingWithError() throws Exception {
        Recording veryBadRecording = new Recording(0, null, RecordingType.VIDEO, null, "album name", 1000, "https://site.com/album-picture.png",
                null, 0, null, null);

        MvcResult result = mvc.perform(post("/api/recordings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(veryBadRecording)))
                .andReturn();

        assertEquals(result.getResponse().getStatus(), 500);
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(), GlobalErrorHandler.MyError.class);

        assertEquals(6, error.getAllErrors().size());

    }

}
