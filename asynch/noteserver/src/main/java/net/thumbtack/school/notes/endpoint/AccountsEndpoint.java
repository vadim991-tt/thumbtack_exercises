package net.thumbtack.school.notes.endpoint;


import com.google.gson.Gson;
import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dto.request.author.ChangePasswordDtoRequest;
import net.thumbtack.school.notes.dto.request.author.DeleteAuthorDtoRequest;
import net.thumbtack.school.notes.dto.request.author.RegisterAuthorDtoRequest;
import net.thumbtack.school.notes.dto.response.author.AuthorDtoResponse;
import net.thumbtack.school.notes.dto.response.author.AuthorInfoDtoResponse;
import net.thumbtack.school.notes.dto.response.author.ChangePasswordDtoResponse;
import net.thumbtack.school.notes.dto.response.author.RegisterAuthorDtoResponse;
import net.thumbtack.school.notes.mapstruct.DtoAuthorMapper;
import net.thumbtack.school.notes.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountsEndpoint {
    private final AuthorService authorService;

    @Autowired
    public AccountsEndpoint(AuthorService authorService) {
        this.authorService = authorService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegisterAuthorDtoResponse registerAuthor(@RequestBody @Valid RegisterAuthorDtoRequest req, HttpServletResponse res) throws Exception {
        String cookieUuid = authorService.registerAuthor(req);
        RegisterAuthorDtoResponse resp = DtoAuthorMapper.INSTANCE.responseFromRequest(req);
        Cookie cookie = new Cookie("JAVASESSIONID", cookieUuid);
        res.addCookie(cookie);
        return resp;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorInfoDtoResponse getAuthorInformation(@CookieValue(value = "JAVASESSIONID") String javaSessionId) throws ServerException {
        return authorService.getUserInfo(javaSessionId);
    }

    @DeleteMapping
    public void deleteAuthor(@RequestBody DeleteAuthorDtoRequest req,
                             @CookieValue(value = "JAVASESSIONID") String javaSessionId) throws ServerException {
        authorService.deleteAuthor(javaSessionId, req.getPassword());
    }

    @PutMapping
    public ChangePasswordDtoResponse changeUserInfo(@RequestBody ChangePasswordDtoRequest req,
                                                    @CookieValue(value = "JAVASESSIONID") String javaSessionId) throws ServerException {
        return authorService.changeUserInfo(req, javaSessionId);
    }

    @PutMapping(value = "/{id}/super")
    public void addSuperPrivileges(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                   @PathVariable("id") int authorId) throws ServerException {
        authorService.addSuperPrivileges(javaSessionId, authorId);
    }

    @GetMapping("/all")
    public List<? extends AuthorDtoResponse> getAuthorInfo(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                                           @RequestParam Map<String, String> allParams) throws ServerException {
        return authorService.getAuthors(javaSessionId, allParams);
    }


}
