package net.thumbtack.school.notes.endpoint;

import net.thumbtack.school.notes.dto.request.author.*;
import net.thumbtack.school.notes.dto.response.author.AuthorDtoResponse;
import net.thumbtack.school.notes.dto.response.author.AuthorInfoDtoResponse;
import net.thumbtack.school.notes.dto.response.author.ChangePasswordDtoResponse;
import net.thumbtack.school.notes.dto.response.author.RegisterAuthorDtoResponse;
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
public class TestAuthorsEndpoint {

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
    public void testRegisterAuthor() {
        RegisterAuthorDtoRequest req = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "login12345", "password12345");
        String url = "http://localhost:8080/api/accounts";

        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, req, RegisterAuthorDtoResponse.class);
        RegisterAuthorDtoResponse dtoResp = resp.getBody();
        HttpHeaders httpHeaders = resp.getHeaders();
        List<String> cookies = httpHeaders.get("Set-Cookie");

        assert cookies != null;
        String javaSessionId = cookies.get(0);

        assertAll(
                () -> assertEquals(req.getFirstName(), dtoResp.getFirstName()),
                () -> assertEquals(req.getLastName(), dtoResp.getLastName()),
                () -> assertEquals(req.getPatronymic(), dtoResp.getPatronymic()),
                () -> assertEquals(req.getLogin(), dtoResp.getLogin()),
                () -> assertTrue(javaSessionId.contains("JAVASESSIONID="))
        );
    }


    @Test
    public void testLogout() {
        RegisterAuthorDtoRequest req = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "login12345", "password12345");
        String url = "http://localhost:8080/api/accounts";

        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, req, RegisterAuthorDtoResponse.class);
        HttpHeaders httpHeaders = resp.getHeaders();
        List<String> cookies = httpHeaders.get("Set-Cookie");
        assert cookies != null;
        String javaSessionId = cookies.get(0);

        url = "http://localhost:8080/api/sessions";
        HttpHeaders httpHeaders1 = new HttpHeaders();
        httpHeaders1.add("Cookie", javaSessionId);

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders1);
        template.exchange(url, HttpMethod.DELETE, entity, String.class);

        try {
            url = "http://localhost:8080/api/accounts";
            ResponseEntity<AuthorInfoDtoResponse> loginResp = template.exchange(url, HttpMethod.GET, entity, AuthorInfoDtoResponse.class);
            fail();
        } catch (HttpClientErrorException exception) {
            assertTrue(Objects.requireNonNull(exception.getMessage()).contains("INVALID_TOKEN"));
        }


    }

    @Test
    public void testLoginLogout() {
        RegisterAuthorDtoRequest req = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "login12345", "password12345");
        String url = "http://localhost:8080/api/accounts";

        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, req, RegisterAuthorDtoResponse.class);
        HttpHeaders httpHeaders = resp.getHeaders();
        List<String> cookies = httpHeaders.get("Set-Cookie");
        assert cookies != null;
        String firstJavaSessionID = cookies.get(0);

        url = "http://localhost:8080/api/sessions";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders1 = new HttpHeaders();
        httpHeaders1.add("Cookie", firstJavaSessionID);

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders1);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);


        LoginAuthorDtoRequest loginReq = new LoginAuthorDtoRequest("login12345", "password12345");
        ResponseEntity<LoginAuthorDtoRequest> logingResp = template.postForEntity(url, loginReq, LoginAuthorDtoRequest.class);
        HttpHeaders httpHeaders2 = logingResp.getHeaders();
        List<String> cookies2 = httpHeaders2.get("Set-Cookie");
        assert cookies2 != null;
        String secondJavaSessionID = cookies2.get(0);

        assertNotNull(firstJavaSessionID);
        assertNotNull(secondJavaSessionID);
        assertNotEquals(firstJavaSessionID, secondJavaSessionID);

    }

    @Test
    public void testGetUserInfo() {
        RegisterAuthorDtoRequest dtoReq = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "login12345", "password12345");
        String url = "http://localhost:8080/api/accounts";

        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, dtoReq, RegisterAuthorDtoResponse.class);
        HttpHeaders respHeaders = resp.getHeaders();
        List<String> cookies = respHeaders.get("Set-Cookie");
        assert cookies != null;
        String javaSessionId = cookies.get(0);

        url = "http://localhost:8080/api/accounts";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add("Cookie", javaSessionId);

        HttpEntity<String> entity = new HttpEntity<>(reqHeaders);
        ResponseEntity<AuthorInfoDtoResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, AuthorInfoDtoResponse.class);
        AuthorInfoDtoResponse dtoResp = responseEntity.getBody();

        assertAll(
                () -> assertEquals(dtoReq.getFirstName(), dtoResp.getFirstName()),
                () -> assertEquals(dtoReq.getLastName(), dtoResp.getLastName()),
                () -> assertEquals(dtoReq.getPatronymic(), dtoResp.getPatronymic()),
                () -> assertEquals(dtoReq.getLogin(), dtoResp.getLogin())
        );


    }

    @Test
    public void deleteAuthor() {
        RegisterAuthorDtoRequest dtoReq = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "login12345", "password12345");
        String url = "http://localhost:8080/api/accounts";

        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, dtoReq, RegisterAuthorDtoResponse.class);
        HttpHeaders respHeaders = resp.getHeaders();
        List<String> cookies = respHeaders.get("Set-Cookie");
        assert cookies != null;
        String javaSessionId = cookies.get(0);

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add("Cookie", javaSessionId);
        DeleteAuthorDtoRequest request = new DeleteAuthorDtoRequest("password12345");
        HttpEntity<DeleteAuthorDtoRequest> entity = new HttpEntity<>(request, reqHeaders);
        ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.DELETE, entity, String.class);

        try {
            url = "http://localhost:8080/api/accounts";
            ResponseEntity<AuthorInfoDtoResponse> loginResp = template.exchange(url, HttpMethod.GET, entity, AuthorInfoDtoResponse.class);
            fail();
        } catch (HttpClientErrorException exception) {
            assertTrue(exception.getMessage().contains("INVALID_TOKEN"));
        }
    }

    @Test
    public void changeProfileInfo() {
        RegisterAuthorDtoRequest dtoReq = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "login12345", "password12345");
        String url = "http://localhost:8080/api/accounts";

        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, dtoReq, RegisterAuthorDtoResponse.class);
        HttpHeaders respHeaders = resp.getHeaders();
        List<String> cookies = respHeaders.get("Set-Cookie");
        assert cookies != null;
        String javaSessionId = cookies.get(0);

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add("Cookie", javaSessionId);
        ChangePasswordDtoRequest request = new ChangePasswordDtoRequest("firstName2", "lastName2", "patronymic2", dtoReq.getPassword(), "myNewPassword");
        HttpEntity<ChangePasswordDtoRequest> entity = new HttpEntity<>(request, reqHeaders);
        ResponseEntity<ChangePasswordDtoResponse> responseEntity = template.exchange(url, HttpMethod.PUT, entity, ChangePasswordDtoResponse.class);
        ChangePasswordDtoResponse changePasswordDtoResponse = responseEntity.getBody();

        HttpEntity<String> infoEntity = new HttpEntity<>(reqHeaders);
        ResponseEntity<AuthorInfoDtoResponse> infoResponseEntity = template.exchange(url, HttpMethod.GET, entity, AuthorInfoDtoResponse.class);
        AuthorInfoDtoResponse infoDtoResponse = infoResponseEntity.getBody();

        assertAll(
                () -> assertEquals(changePasswordDtoResponse.getFirstName(), infoDtoResponse.getFirstName()),
                () -> assertEquals(changePasswordDtoResponse.getLastName(), infoDtoResponse.getLastName()),
                () -> assertEquals(changePasswordDtoResponse.getPatronymic(), infoDtoResponse.getPatronymic())
        );
    }

    @Test
    public void getAuthorList() {
        String url = "http://localhost:8080/api/accounts";
        RegisterAuthorDtoRequest dtoReq = new RegisterAuthorDtoRequest("FirstName", "LastName", "Patronymic", "login12345", "password12345");
        for (int i = 0; i < 10; i++) {
            dtoReq.setLogin(String.format("login12345%s", i));
            template.postForEntity(url, dtoReq, RegisterAuthorDtoResponse.class);
        }

        dtoReq.setLogin("uniqueLogin12345");
        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, dtoReq, RegisterAuthorDtoResponse.class);
        HttpHeaders respHeaders = resp.getHeaders();
        List<String> cookies = respHeaders.get("Set-Cookie");
        assert cookies != null;
        String javaSessionId = cookies.get(0);

        url = "http://localhost:8080/api/accounts/all";
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add("Cookie", javaSessionId);
        HttpEntity<String> entity = new HttpEntity<>(reqHeaders);
        ResponseEntity<AuthorDtoResponse[]> resp2 = template.exchange(url, HttpMethod.GET, entity, AuthorDtoResponse[].class);
        assertEquals(resp2.getBody().length, 11);
    }

    @Test
    public void followUnfollowAuthor() {
        String url = "http://localhost:8080/api/accounts";
        RegisterAuthorDtoRequest authorToFollow = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "loginToFollow12345", "password12345");
        template.postForEntity(url, authorToFollow, RegisterAuthorDtoResponse.class);
        new RegisterAuthorDtoRequest("FirstName", "LastName", "Patronymic", "login12345", "password12345");

        RegisterAuthorDtoRequest dto;
        for (int i = 0; i < 10; i++) {
            dto = new RegisterAuthorDtoRequest("FirstName", "LastName", "Patronymic", String.format("login12345%s", i), "password12345");
            template.postForEntity(url, dto, RegisterAuthorDtoResponse.class);
        }

        RegisterAuthorDtoRequest dtoReq = new RegisterAuthorDtoRequest("FirstName", "LastName", "Patronymic", "uniqueLogin12345", "password12345");
        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, dtoReq, RegisterAuthorDtoResponse.class);
        HttpHeaders respHeaders = resp.getHeaders();
        List<String> cookies = respHeaders.get("Set-Cookie");
        assert cookies != null;
        String javaSessionId = cookies.get(0);

        url = "http://localhost:8080/api/followings";
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add("Cookie", javaSessionId);
        FollowAuthorDtoRequest followAuthorDtoRequest = new FollowAuthorDtoRequest(authorToFollow.getLogin());
        HttpEntity<FollowAuthorDtoRequest> entity = new HttpEntity<>(followAuthorDtoRequest, reqHeaders);
        template.exchange(url, HttpMethod.POST, entity, String.class);

        url = "http://localhost:8080/api/accounts/all";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("type", "following");
        ResponseEntity<AuthorDtoResponse[]> resp2 = template.exchange(builder.toUriString(), HttpMethod.GET, entity, AuthorDtoResponse[].class);
        assertEquals(Objects.requireNonNull(resp2.getBody()).length, 1);

        url = "http://localhost:8080/api/followings/loginToFollow12345";
        HttpEntity<FollowAuthorDtoRequest> unfollowEntity = new HttpEntity<>(reqHeaders);
        template.exchange(url, HttpMethod.DELETE, unfollowEntity, String.class);

        ResponseEntity<AuthorDtoResponse[]> resp3 = template.exchange(builder.toUriString(), HttpMethod.GET, entity, AuthorDtoResponse[].class);
        assertEquals(Objects.requireNonNull(resp3.getBody()).length, 0);
    }

    @Test
    public void ignoreStopIgnoringAuthor(){
        String url = "http://localhost:8080/api/accounts";
        RegisterAuthorDtoRequest authorToFollow = new RegisterAuthorDtoRequest("FirstName", "LastName",
                "Patronymic", "loginToIgnore12345", "password12345");
        template.postForEntity(url, authorToFollow, RegisterAuthorDtoResponse.class);
        new RegisterAuthorDtoRequest("FirstName", "LastName", "Patronymic", "login12345", "password12345");

        RegisterAuthorDtoRequest dto;
        for (int i = 0; i < 10; i++) {
            dto = new RegisterAuthorDtoRequest("FirstName", "LastName", "Patronymic", String.format("login12345%s", i), "password12345");
            template.postForEntity(url, dto, RegisterAuthorDtoResponse.class);
        }

        RegisterAuthorDtoRequest dtoReq = new RegisterAuthorDtoRequest("FirstName", "LastName", "Patronymic",
                "uniqueLogin12345", "password12345");
        ResponseEntity<RegisterAuthorDtoResponse> resp = template.postForEntity(url, dtoReq, RegisterAuthorDtoResponse.class);
        HttpHeaders respHeaders = resp.getHeaders();
        List<String> cookies = respHeaders.get("Set-Cookie");
        assert cookies != null;
        String javaSessionId = cookies.get(0);

        url = "http://localhost:8080/api/ignore";
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add("Cookie", javaSessionId);
        FollowAuthorDtoRequest followAuthorDtoRequest = new FollowAuthorDtoRequest(authorToFollow.getLogin());
        HttpEntity<FollowAuthorDtoRequest> entity = new HttpEntity<>(followAuthorDtoRequest, reqHeaders);
        template.exchange(url, HttpMethod.POST, entity, String.class);

        url = "http://localhost:8080/api/accounts/all";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("type", "ignore");
        ResponseEntity<AuthorDtoResponse[]> resp2 = template.exchange(builder.toUriString(), HttpMethod.GET, entity, AuthorDtoResponse[].class);
        assertEquals(Objects.requireNonNull(resp2.getBody()).length, 1);

        url = "http://localhost:8080/api/ignore/loginToIgnore12345";
        HttpEntity<FollowAuthorDtoRequest> stopIgnoringEntity = new HttpEntity<>(reqHeaders);
        template.exchange(url, HttpMethod.DELETE, stopIgnoringEntity, String.class);

        ResponseEntity<AuthorDtoResponse[]> resp3 = template.exchange(builder.toUriString(), HttpMethod.GET, entity, AuthorDtoResponse[].class);
        assertEquals(Objects.requireNonNull(resp3.getBody()).length, 0);
    }

}
