package net.thumbtack.school.notes.endpoint;

import net.thumbtack.school.notes.dto.request.author.*;
import net.thumbtack.school.notes.dto.response.author.AuthorDtoResponse;
import net.thumbtack.school.notes.dto.response.author.AuthorInfoDtoResponse;
import net.thumbtack.school.notes.dto.response.author.ChangePasswordDtoResponse;
import net.thumbtack.school.notes.dto.response.author.RegisterAuthorDtoResponse;
import net.thumbtack.school.notes.dto.response.server.ServerSettingsDtoResponse;
import net.thumbtack.school.notes.utils.MyBatisUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestDebugEndpoint {

    private final RestTemplate template = new RestTemplate();
    private static boolean setUpIsDone = false;

    @BeforeEach()
    public void clearDatabase() {
        template.postForObject("http://localhost:8080/api/debug/clear", null, Object.class);
    }

    @BeforeAll()
    public static void setUp() {
        if (!setUpIsDone) {
            boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();
            if (!initSqlSessionFactory) {
                throw new RuntimeException("Can't create connection, stop");
            }
            setUpIsDone = true;
        }
    }

    @Test
    public void getSettings(){
        String url = "http://localhost:8080/api/debug/settings";
        ResponseEntity<ServerSettingsDtoResponse> entity = template.getForEntity(url, ServerSettingsDtoResponse.class);
        assertNotNull(entity.getBody());
    }
}
