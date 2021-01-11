package net.thumbtack.school.notes.endpoint;


import com.google.gson.Gson;
import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dto.request.author.LoginAuthorDtoRequest;
import net.thumbtack.school.notes.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/sessions")
public class SessionsEndpoint {
    private final AuthorService authorService;

    @Autowired
    public SessionsEndpoint(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerAuthor(@RequestBody @Valid LoginAuthorDtoRequest req, HttpServletResponse res) throws ServerException {
        String javaSessionId = authorService.loginAuthor(req.getLogin(), req.getPassword());
        Cookie cookie = new Cookie("JAVASESSIONID", javaSessionId);
        res.addCookie(cookie);
    }

    @DeleteMapping
    public void logout(@CookieValue(value = "JAVASESSIONID") String javaSessionId) throws ServerException {
        authorService.logout(javaSessionId);
    }


}
