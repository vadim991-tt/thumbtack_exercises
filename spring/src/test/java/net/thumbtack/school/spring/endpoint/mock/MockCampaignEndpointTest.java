package net.thumbtack.school.spring.endpoint.mock;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.thumbtack.school.spring.endpoint.CampaignEndpoint;
import net.thumbtack.school.spring.endpoint.RecordingsEndpoint;
import net.thumbtack.school.spring.model.DtoResponse;
import net.thumbtack.school.spring.model.Recording;
import net.thumbtack.school.spring.model.RecordingType;
import net.thumbtack.school.spring.service.PromotionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CampaignEndpoint.class)
public class MockCampaignEndpointTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PromotionServiceImpl promotionService;

    @Test
    public void testStartCampaign() throws Exception {
        String songName = "RandomSongName";
        Recording recording = new Recording(0, "artist", RecordingType.VIDEO, songName, "album name", 1999, "https://site.com/album-picture.png",
                "Rock", 300, "~/songs", null);

        MvcResult result = mvc.perform(post("/api/campaigns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recording)))
                .andReturn();

        String exceptedResponseMessage = String.format("Campaign for %s was started successfully", songName);
        String stringResult = result.getResponse().getContentAsString();
        DtoResponse response = new Gson().fromJson(stringResult, DtoResponse.class);

        assertEquals(exceptedResponseMessage, response.getMessage());
    }

    @Test
    public void testStopCampaign() throws Exception {
        int id = new Random().nextInt(10);

        MvcResult result = mvc.perform(put(String.format("/api/campaigns/%s/status/stop", id))).andReturn();
        String exceptedResponseMessage = String.format("Campaign with %s ID was stopped successfully", id);
        String stringResult = result.getResponse().getContentAsString();
        DtoResponse response = new Gson().fromJson(stringResult, DtoResponse.class);

        assertEquals(exceptedResponseMessage, response.getMessage());
    }

    @Test
    public void testDeleteCampaign() throws Exception {
        int id = new Random().nextInt(10);

        MvcResult result = mvc.perform(delete(String.format("/api/campaigns/%s", id))).andReturn();
        String exceptedResponseMessage = String.format("Campaign with %s ID was deleted successfully", id);
        String stringResult = result.getResponse().getContentAsString();
        DtoResponse response = new Gson().fromJson(stringResult, DtoResponse.class);

        assertEquals(exceptedResponseMessage, response.getMessage());
    }


}


