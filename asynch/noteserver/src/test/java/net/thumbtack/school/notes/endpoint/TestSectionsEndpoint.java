package net.thumbtack.school.notes.endpoint;

import net.thumbtack.school.notes.dto.request.author.RegisterAuthorDtoRequest;
import net.thumbtack.school.notes.dto.request.section.CreateSectionDtoRequest;
import net.thumbtack.school.notes.dto.request.section.RenameSectionDtoRequest;
import net.thumbtack.school.notes.dto.response.author.RegisterAuthorDtoResponse;
import net.thumbtack.school.notes.dto.response.section.CreateSectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.SectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.RenameSectionDtoResponse;
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestSectionsEndpoint {

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

    public static HttpHeaders getUUIDHeaders() {
        RegisterAuthorDtoRequest req = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "login12345", "password12345");
        String url = "http://localhost:8080/api/accounts";

        RestTemplate template = new RestTemplate();
        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, req, RegisterAuthorDtoResponse.class);
        HttpHeaders httpHeaders = resp.getHeaders();
        List<String> cookies = httpHeaders.get("Set-Cookie");
        assert cookies != null;
        String javaSessionId = cookies.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", javaSessionId);
        return headers;
    }

    @Test
    public void createSection() {
        HttpHeaders headers = getUUIDHeaders();
        String url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req = new CreateSectionDtoRequest("name");
        HttpEntity<CreateSectionDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateSectionDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class);

        assertNotEquals(0, Objects.requireNonNull(responseEntity.getBody()).getId());
    }

    @Test
    public void createSectionError(){
        HttpHeaders headers = getUUIDHeaders();
        String url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req = new CreateSectionDtoRequest("name");
        HttpEntity<CreateSectionDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateSectionDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class);

        assertThrows(HttpClientErrorException.class, () -> template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class));
    }

    @Test
    public void renameSection(){
        HttpHeaders headers = getUUIDHeaders();
        String url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req = new CreateSectionDtoRequest("name");
        HttpEntity<CreateSectionDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateSectionDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class);

        RenameSectionDtoRequest renameReq = new RenameSectionDtoRequest("newName");
        url = String.format("http://localhost:8080/api/sections/%s", responseEntity.getBody().getId());
        HttpEntity<RenameSectionDtoRequest> entity1 = new HttpEntity<>(renameReq, headers);
        ResponseEntity<RenameSectionDtoResponse> renameEntity = template.exchange(url, HttpMethod.PUT, entity1, RenameSectionDtoResponse.class);

        assertEquals(responseEntity.getBody().getId(), renameEntity.getBody().getId());
    }

    @Test
    public void deleteSection(){
        HttpHeaders headers = getUUIDHeaders();
        String url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req = new CreateSectionDtoRequest("name");
        HttpEntity<CreateSectionDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateSectionDtoResponse> createSectionResp1 = template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class);

        String deleteUrl = String.format("http://localhost:8080/api/sections/%s", createSectionResp1.getBody().getId());
        HttpEntity<String> entity1 = new HttpEntity<>(headers);
        template.exchange(deleteUrl, HttpMethod.DELETE, entity, String.class);

        ResponseEntity<CreateSectionDtoResponse> createSectionResp2 = template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class);
        assertNotEquals(createSectionResp1.getBody().getId(), createSectionResp2.getBody().getId());
    }

    @Test
    public void getInfo(){
        HttpHeaders headers = getUUIDHeaders();
        String url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req = new CreateSectionDtoRequest("name");
        HttpEntity<CreateSectionDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateSectionDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class);

        HttpEntity<String> entity1 = new HttpEntity<>(headers);
        url = String.format("http://localhost:8080/api/sections/%s", responseEntity.getBody().getId());
        ResponseEntity<SectionDtoResponse> infoResponseEntity = template.exchange(url, HttpMethod.GET, entity, SectionDtoResponse.class);
        assertEquals(responseEntity.getBody().getId(), infoResponseEntity.getBody().getId());
    }

    @Test
    public void getSections(){
        HttpHeaders headers = getUUIDHeaders();
        String url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req;
        for(int i = 0; i < 10; i++){
            req = new CreateSectionDtoRequest(String.format("name%s", i));
            HttpEntity<CreateSectionDtoRequest> entity = new HttpEntity<>(req, headers);
            template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class);
        }
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<SectionDtoResponse[]> responseEntity = template.exchange(url, HttpMethod.GET, entity, SectionDtoResponse[].class);
        assertEquals(10, Objects.requireNonNull(responseEntity.getBody()).length);
    }

}
