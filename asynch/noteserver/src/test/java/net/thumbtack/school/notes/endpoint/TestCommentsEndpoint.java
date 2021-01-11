package net.thumbtack.school.notes.endpoint;

import net.thumbtack.school.notes.dto.request.author.RegisterAuthorDtoRequest;
import net.thumbtack.school.notes.dto.request.comment.CreateCommentDtoRequest;
import net.thumbtack.school.notes.dto.request.comment.EditCommentDtoRequest;
import net.thumbtack.school.notes.dto.request.note.CreateNoteDtoRequest;
import net.thumbtack.school.notes.dto.request.section.CreateSectionDtoRequest;
import net.thumbtack.school.notes.dto.response.author.RegisterAuthorDtoResponse;
import net.thumbtack.school.notes.dto.response.comment.CommentDtoResponse;
import net.thumbtack.school.notes.dto.response.comment.CreateCommentDtoResponse;
import net.thumbtack.school.notes.dto.response.comment.EditCommentDtoResponse;
import net.thumbtack.school.notes.dto.response.note.CreateNoteDtoResponse;
import net.thumbtack.school.notes.dto.response.notes.CommentDto;
import net.thumbtack.school.notes.dto.response.section.CreateSectionDtoResponse;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestCommentsEndpoint {

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

    public static int createNoteAndObtainId(HttpHeaders headers) {
        RestTemplate template = new RestTemplate();
        String  url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req2 = new CreateSectionDtoRequest("name");
        HttpEntity<CreateSectionDtoRequest> entity = new HttpEntity<>(req2, headers);
        ResponseEntity<CreateSectionDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class);

        url = "http://localhost:8080/api/notes";
        CreateNoteDtoRequest req3 = new CreateNoteDtoRequest("subject", "body", responseEntity.getBody().getId());
        HttpEntity<CreateNoteDtoRequest> entity3 = new HttpEntity<>(req3, headers);
        ResponseEntity<CreateNoteDtoResponse> createNoteResp = template.exchange(url, HttpMethod.POST, entity3, CreateNoteDtoResponse.class);
        return createNoteResp.getBody().getId();
    }


    @Test
    public void createComment() {
        HttpHeaders httpHeaders = getUUIDHeaders();
        int noteId = createNoteAndObtainId(httpHeaders);

        String url = "http://localhost:8080/api/comments";
        CreateCommentDtoRequest createReq = new CreateCommentDtoRequest("This is my comment", noteId);
        HttpEntity<CreateCommentDtoRequest> createEntity = new HttpEntity<>(createReq, httpHeaders);
        ResponseEntity<CreateCommentDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, createEntity, CreateCommentDtoResponse.class);
        assertEquals(noteId, responseEntity.getBody().getNoteId());
    }

    @Test
    public void getComments() {
        HttpHeaders httpHeaders = getUUIDHeaders();
        int noteId = createNoteAndObtainId(httpHeaders);

        String url = "http://localhost:8080/api/comments";
        for (int i = 0; i < 10; i++) {
            CreateCommentDtoRequest createReq = new CreateCommentDtoRequest("This is my comment" + i, noteId);
            HttpEntity<CreateCommentDtoRequest> createEntity = new HttpEntity<>(createReq, httpHeaders);
            template.exchange(url, HttpMethod.POST, createEntity, CreateCommentDtoResponse.class);
        }
        url = String.format("http://localhost:8080/api/notes/%s/comments", noteId);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<CommentDtoResponse[]> responseEntity = template.exchange(url, HttpMethod.GET, entity, CommentDtoResponse[].class);
        assertEquals(10, responseEntity.getBody().length);
    }

    @Test
    public void changeCommentBody() {
        HttpHeaders httpHeaders = getUUIDHeaders();
        int noteId = createNoteAndObtainId(httpHeaders);

        String url = "http://localhost:8080/api/comments";
        CreateCommentDtoRequest createReq = new CreateCommentDtoRequest("This is my comment", noteId);
        HttpEntity<CreateCommentDtoRequest> createEntity = new HttpEntity<>(createReq, httpHeaders);
        ResponseEntity<CreateCommentDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, createEntity, CreateCommentDtoResponse.class);
        String bodyBefore = responseEntity.getBody().getBody();

        url = "http://localhost:8080/api/comments/" + responseEntity.getBody().getId();
        EditCommentDtoRequest request = new EditCommentDtoRequest("newBody");
        HttpEntity<EditCommentDtoRequest> editEntity = new HttpEntity<>(request, httpHeaders);
        ResponseEntity<EditCommentDtoResponse> responseEntity1 = template.exchange(url, HttpMethod.PUT, editEntity, EditCommentDtoResponse.class);
        String bodyAfter = responseEntity1.getBody().getBody();

        assertNotEquals(bodyBefore, bodyAfter);
    }

    @Test
    public void deleteComment() {
        HttpHeaders httpHeaders = getUUIDHeaders();
        int noteId = createNoteAndObtainId(httpHeaders);

        String url = "http://localhost:8080/api/comments";
        CreateCommentDtoRequest createReq = new CreateCommentDtoRequest("This is my comment", noteId);
        HttpEntity<CreateCommentDtoRequest> createEntity = new HttpEntity<>(createReq, httpHeaders);
        ResponseEntity<CreateCommentDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, createEntity, CreateCommentDtoResponse.class);

        url = String.format("http://localhost:8080/api/notes/%s/comments", noteId);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<CommentDtoResponse[]> responseEntity1 = template.exchange(url, HttpMethod.GET, entity, CommentDtoResponse[].class);

        assertEquals(1, responseEntity1.getBody().length);

        String deleteUrl = "http://localhost:8080/api/comments/" + responseEntity.getBody().getId();
        HttpEntity<String> deleteEntity =  new HttpEntity<>(httpHeaders);
        template.exchange(deleteUrl, HttpMethod.DELETE, deleteEntity, String.class);

        entity = new HttpEntity<>(httpHeaders);
        responseEntity1 = template.exchange(url, HttpMethod.GET, entity, CommentDtoResponse[].class);

        assertEquals(0, responseEntity1.getBody().length);
    }

    @Test
    public void deleteCommentsFromNote(){
        HttpHeaders httpHeaders = getUUIDHeaders();
        int noteId = createNoteAndObtainId(httpHeaders);

        String url = "http://localhost:8080/api/comments";
        for (int i = 0; i < 10; i++) {
            CreateCommentDtoRequest createReq = new CreateCommentDtoRequest("This is my comment" + i, noteId);
            HttpEntity<CreateCommentDtoRequest> createEntity = new HttpEntity<>(createReq, httpHeaders);
            template.exchange(url, HttpMethod.POST, createEntity, CreateCommentDtoResponse.class);
        }
        url = String.format("http://localhost:8080/api/notes/%s/comments", noteId);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<CommentDtoResponse[]> responseEntity = template.exchange(url, HttpMethod.GET, entity, CommentDtoResponse[].class);
        assertEquals(10, responseEntity.getBody().length);

        template.exchange(url, HttpMethod.DELETE, entity, String.class);
        ResponseEntity<CommentDtoResponse[]> responseEntity2 = template.exchange(url, HttpMethod.GET, entity, CommentDtoResponse[].class);
        assertEquals(0, responseEntity2.getBody().length);
    }


}