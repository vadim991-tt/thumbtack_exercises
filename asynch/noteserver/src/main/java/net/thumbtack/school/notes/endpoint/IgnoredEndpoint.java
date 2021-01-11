package net.thumbtack.school.notes.endpoint;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dto.request.author.FollowAuthorDtoRequest;
import net.thumbtack.school.notes.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ignore")
public class IgnoredEndpoint {


    private final AuthorService authorService;

    @Autowired
    public IgnoredEndpoint(AuthorService authorService){
        this.authorService = authorService;
    }

    @PostMapping
    public void ignoreAuthor(@CookieValue(value = "JAVASESSIONID") String javaSessionId, @RequestBody FollowAuthorDtoRequest dto) throws ServerException {
        authorService.ignoreAuthor(javaSessionId, dto.getLogin());
    }

    @DeleteMapping("/{login}")
    public void unfollowAuthor(@CookieValue(value = "JAVASESSIONID") String javaSessionId, @PathVariable("login") String login) throws ServerException {
        authorService.stopIgnoringAuthor(javaSessionId, login);
    }

}
