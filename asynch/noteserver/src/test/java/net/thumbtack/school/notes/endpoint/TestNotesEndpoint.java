package net.thumbtack.school.notes.endpoint;

import net.thumbtack.school.notes.dto.request.author.RegisterAuthorDtoRequest;
import net.thumbtack.school.notes.dto.request.comment.CreateCommentDtoRequest;
import net.thumbtack.school.notes.dto.request.note.ChangeNoteDtoRequest;
import net.thumbtack.school.notes.dto.request.note.CreateNoteDtoRequest;
import net.thumbtack.school.notes.dto.request.note.RateNoteDtoRequest;
import net.thumbtack.school.notes.dto.request.section.CreateSectionDtoRequest;
import net.thumbtack.school.notes.dto.request.section.RenameSectionDtoRequest;
import net.thumbtack.school.notes.dto.response.author.RegisterAuthorDtoResponse;
import net.thumbtack.school.notes.dto.response.comment.CreateCommentDtoResponse;
import net.thumbtack.school.notes.dto.response.note.ChangeNoteDtoResponse;
import net.thumbtack.school.notes.dto.response.note.CreateNoteDtoResponse;
import net.thumbtack.school.notes.dto.response.note.NoteInfoDtoResponse;
import net.thumbtack.school.notes.dto.response.notes.CommentDto;
import net.thumbtack.school.notes.dto.response.notes.NoteDto;
import net.thumbtack.school.notes.dto.response.notes.RevisionDto;
import net.thumbtack.school.notes.dto.response.section.CreateSectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.SectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.RenameSectionDtoResponse;
import net.thumbtack.school.notes.model.Revision;
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestNotesEndpoint {

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

    public static int createSectionAndObtainId() {
        RegisterAuthorDtoRequest req = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "uniqueLogin12345", "password12345");
        String url = "http://localhost:8080/api/accounts";

        RestTemplate template = new RestTemplate();
        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, req, RegisterAuthorDtoResponse.class);
        HttpHeaders httpHeaders = resp.getHeaders();
        List<String> cookies = httpHeaders.get("Set-Cookie");
        assert cookies != null;
        String javaSessionId = cookies.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", javaSessionId);

        url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req2 = new CreateSectionDtoRequest("name");
        HttpEntity<CreateSectionDtoRequest> entity = new HttpEntity<>(req2, headers);
        ResponseEntity<CreateSectionDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateSectionDtoResponse.class);
        return responseEntity.getBody().getId();
    }

    @Test
    public void createNote() {
        String url = "http://localhost:8080/api/notes";
        HttpHeaders headers = getUUIDHeaders();
        int sectionId = createSectionAndObtainId();

        CreateNoteDtoRequest req = new CreateNoteDtoRequest("subject", "body", sectionId);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);
        assertNotEquals(0, responseEntity.getBody().getId());
        assertEquals(sectionId, responseEntity.getBody().getSectionId());
    }

    @Test
    public void getNoteInfo() {
        String url = "http://localhost:8080/api/notes";
        HttpHeaders headers = getUUIDHeaders();
        int sectionId = createSectionAndObtainId();

        CreateNoteDtoRequest req = new CreateNoteDtoRequest("subject", "body", sectionId);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);

        url = String.format("http://localhost:8080/api/notes/%s", responseEntity.getBody().getId());
        HttpEntity<String> secondEntity = new HttpEntity<>(headers);
        ResponseEntity<NoteInfoDtoResponse> secondResponseEntity = template.exchange(url, HttpMethod.GET, secondEntity, NoteInfoDtoResponse.class);
        assertEquals(responseEntity.getBody().getId(), secondResponseEntity.getBody().getId());
    }

    @Test
    public void changeBody() {
        String url = "http://localhost:8080/api/notes";
        HttpHeaders headers = getUUIDHeaders();
        int sectionId = createSectionAndObtainId();

        CreateNoteDtoRequest req = new CreateNoteDtoRequest("subject", "body", sectionId);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);

        url = String.format("http://localhost:8080/api/notes/%s", responseEntity.getBody().getId());
        ChangeNoteDtoRequest req2 = new ChangeNoteDtoRequest("newBody", null);
        HttpEntity<ChangeNoteDtoRequest> entity2 = new HttpEntity<>(req2, headers);
        ResponseEntity<ChangeNoteDtoResponse> responseEntity2 = template.exchange(url, HttpMethod.PUT, entity2, ChangeNoteDtoResponse.class);

        assertNotEquals(responseEntity.getBody().getBody(), responseEntity2.getBody().getBody());
        assertEquals(responseEntity.getBody().getSectionId(), responseEntity2.getBody().getSectionId());
    }

    @Test
    public void moveNote() {
        String url = "http://localhost:8080/api/notes";
        HttpHeaders headers = getUUIDHeaders();
        int firstSectionID = createSectionAndObtainId();

        CreateNoteDtoRequest req = new CreateNoteDtoRequest("subject", "body", firstSectionID);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);

        url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req2 = new CreateSectionDtoRequest("uniqueName");
        HttpEntity<CreateSectionDtoRequest> entity2 = new HttpEntity<>(req2, headers);
        ResponseEntity<CreateSectionDtoResponse> responseEntity2 = template.exchange(url, HttpMethod.POST, entity2, CreateSectionDtoResponse.class);
        int secondSectionID = responseEntity2.getBody().getId();

        url = String.format("http://localhost:8080/api/notes/%s", responseEntity.getBody().getId());
        ChangeNoteDtoRequest req3 = new ChangeNoteDtoRequest(null, secondSectionID);
        HttpEntity<ChangeNoteDtoRequest> entity3 = new HttpEntity<>(req3, headers);
        ResponseEntity<ChangeNoteDtoResponse> responseEntity3 = template.exchange(url, HttpMethod.PUT, entity3, ChangeNoteDtoResponse.class);

        assertNotEquals(responseEntity.getBody().getSectionId(), responseEntity3.getBody().getSectionId());
        assertEquals(responseEntity.getBody().getBody(), responseEntity3.getBody().getBody());
    }

    @Test
    public void changeBodyAndMoveNote() {
        String url = "http://localhost:8080/api/notes";
        HttpHeaders headers = getUUIDHeaders();
        int firstSectionID = createSectionAndObtainId();

        CreateNoteDtoRequest req = new CreateNoteDtoRequest("subject", "body", firstSectionID);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);

        url = "http://localhost:8080/api/sections";
        CreateSectionDtoRequest req2 = new CreateSectionDtoRequest("uniqueName");
        HttpEntity<CreateSectionDtoRequest> entity2 = new HttpEntity<>(req2, headers);
        ResponseEntity<CreateSectionDtoResponse> responseEntity2 = template.exchange(url, HttpMethod.POST, entity2, CreateSectionDtoResponse.class);
        int secondSectionID = responseEntity2.getBody().getId();

        url = String.format("http://localhost:8080/api/notes/%s", responseEntity.getBody().getId());
        ChangeNoteDtoRequest req3 = new ChangeNoteDtoRequest("newBody", secondSectionID);
        HttpEntity<ChangeNoteDtoRequest> entity3 = new HttpEntity<>(req3, headers);
        ResponseEntity<ChangeNoteDtoResponse> responseEntity3 = template.exchange(url, HttpMethod.PUT, entity3, ChangeNoteDtoResponse.class);

        assertNotEquals(responseEntity.getBody().getSectionId(), responseEntity3.getBody().getSectionId());
        assertNotEquals(responseEntity.getBody().getBody(), responseEntity3.getBody().getBody());
    }

    @Test
    public void testDeleteNote() {
        String url = "http://localhost:8080/api/notes";
        HttpHeaders headers = getUUIDHeaders();
        int sectionId = createSectionAndObtainId();

        CreateNoteDtoRequest req = new CreateNoteDtoRequest("subject", "body", sectionId);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);

        url = String.format("http://localhost:8080/api/notes/%s", responseEntity.getBody().getId());
        HttpEntity<String> deleteEntity = new HttpEntity<>(headers);
        template.exchange(url, HttpMethod.DELETE, deleteEntity, String.class);

        HttpEntity<String> secondEntity = new HttpEntity<>(headers);
        ResponseEntity<NoteInfoDtoResponse> secondResponseEntity = template.exchange(url, HttpMethod.GET, secondEntity, NoteInfoDtoResponse.class);
        assertNull(secondResponseEntity.getBody());
    }


    @Test
    public void testRateNote() {
        String url = "http://localhost:8080/api/notes";
        HttpHeaders headers = getUUIDHeaders();
        int sectionId = createSectionAndObtainId();

        for (int i = 0; i < 10; i++) {
            CreateNoteDtoRequest req = new CreateNoteDtoRequest("usualSubject" + i, "usualBody", sectionId);
            HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
            template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);
        }
        CreateNoteDtoRequest req = new CreateNoteDtoRequest("SubjectFromRatedNote", "USHOULDRATEIT!", sectionId);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> responseEntity = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);

        url = String.format("http://localhost:8080/api/notes/%s/rating", responseEntity.getBody().getId());
        RateNoteDtoRequest request = new RateNoteDtoRequest(5);
        HttpEntity<RateNoteDtoRequest> rateEntity = new HttpEntity<>(request, headers);
        template.exchange(url, HttpMethod.POST, rateEntity, String.class);

        url = "http://localhost:8080/api/notes";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("sortByRating", "asc");
        HttpEntity<String> getEntity = new HttpEntity<>(headers);
        ResponseEntity<NoteDto[]> responseEntity1 = template.exchange(builder.toUriString(), HttpMethod.GET, getEntity, NoteDto[].class);
        NoteDto[] dtos = responseEntity1.getBody();

        assert dtos != null;
        String subject = dtos[dtos.length - 1].getSubject();

        assertEquals("SubjectFromRatedNote", subject);

        builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("sortByRating", "desc");
        responseEntity1 = template.exchange(builder.toUriString(), HttpMethod.GET, getEntity, NoteDto[].class);
        dtos = responseEntity1.getBody();
        assert dtos != null;
        subject = dtos[0].getSubject();

        assertEquals("SubjectFromRatedNote", subject);
    }

    @Test
    public void testGetNotesWithRevisionsAndComments() {
        String url;
        HttpHeaders headers = getUUIDHeaders();
        int sectionId = createSectionAndObtainId();


        url = "http://localhost:8080/api/notes";
        CreateNoteDtoRequest req = new CreateNoteDtoRequest("usualSubject", "usualBody", sectionId);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> resp = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);

        url = "http://localhost:8080/api/comments";
        CreateCommentDtoRequest rq = new CreateCommentDtoRequest("comment", resp.getBody().getId());
        HttpEntity<CreateCommentDtoRequest> entity1 = new HttpEntity<>(rq, headers);
        template.exchange(url, HttpMethod.POST, entity1, CreateCommentDtoResponse.class);


        url = "http://localhost:8080/api/notes";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("allVersions", true)
                .queryParam("comments", true);

        HttpEntity<String> getEntity = new HttpEntity<>(headers);
        ResponseEntity<NoteDto[]> responseEntity1 = template.exchange(builder.toUriString(), HttpMethod.GET, getEntity, NoteDto[].class);

        NoteDto[] dtos = responseEntity1.getBody();
        NoteDto note = dtos[0];
        RevisionDto revision = note.getRevisions().get(0);
        CommentDto commentDto = revision.getComments().get(0);

        assertEquals("comment", commentDto.getBody());
        assertNull(note.getComments());
    }

    @Test
    public void testGetNotesWithCommentsOnly() {
        String url;
        HttpHeaders headers = getUUIDHeaders();
        int sectionId = createSectionAndObtainId();


        url = "http://localhost:8080/api/notes";
        CreateNoteDtoRequest req = new CreateNoteDtoRequest("usualSubject", "usualBody", sectionId);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> resp = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);

        url = "http://localhost:8080/api/comments";
        CreateCommentDtoRequest rq = new CreateCommentDtoRequest("comment", resp.getBody().getId());
        HttpEntity<CreateCommentDtoRequest> entity1 = new HttpEntity<>(rq, headers);
        template.exchange(url, HttpMethod.POST, entity1, CreateCommentDtoResponse.class);


        url = "http://localhost:8080/api/notes";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("comments", true);

        HttpEntity<String> getEntity = new HttpEntity<>(headers);
        ResponseEntity<NoteDto[]> responseEntity1 = template.exchange(builder.toUriString(), HttpMethod.GET, getEntity, NoteDto[].class);

        NoteDto[] dtos = responseEntity1.getBody();
        NoteDto note = dtos[0];
        List<RevisionDto> revisions = note.getRevisions();
        List<CommentDto> comments = note.getComments();

        assertNull(revisions);
        assertEquals(1, comments.size());
    }

    @Test
    public void testGetWithCommentsWithAndRevisionIDS() {
        String url;
        HttpHeaders headers = getUUIDHeaders();
        int sectionId = createSectionAndObtainId();

        url = "http://localhost:8080/api/notes";
        CreateNoteDtoRequest req = new CreateNoteDtoRequest("usualSubject", "usualBody", sectionId);
        HttpEntity<CreateNoteDtoRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CreateNoteDtoResponse> resp = template.exchange(url, HttpMethod.POST, entity, CreateNoteDtoResponse.class);

        url = "http://localhost:8080/api/comments";
        CreateCommentDtoRequest rq = new CreateCommentDtoRequest("comment", resp.getBody().getId());
        HttpEntity<CreateCommentDtoRequest> entity1 = new HttpEntity<>(rq, headers);
        template.exchange(url, HttpMethod.POST, entity1, CreateCommentDtoResponse.class);


        url = "http://localhost:8080/api/notes";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("comments", true)
                .queryParam("commentVersion", true);

        HttpEntity<String> getEntity = new HttpEntity<>(headers);
        ResponseEntity<NoteDto[]> responseEntity1 = template.exchange(builder.toUriString(), HttpMethod.GET, getEntity, NoteDto[].class);

        NoteDto[] dtos = responseEntity1.getBody();
        NoteDto note = dtos[0];
        List<RevisionDto> revisions = note.getRevisions();
        List<CommentDto> comments = note.getComments();

        assertNull(revisions);
        assertEquals(1, comments.size());
        assertNotEquals(0, comments.get(0).getRevisionId());
    }



}