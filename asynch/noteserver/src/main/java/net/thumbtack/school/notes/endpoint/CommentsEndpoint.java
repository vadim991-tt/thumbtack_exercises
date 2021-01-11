package net.thumbtack.school.notes.endpoint;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dao.CommentDao;
import net.thumbtack.school.notes.dto.request.comment.CreateCommentDtoRequest;
import net.thumbtack.school.notes.dto.request.comment.EditCommentDtoRequest;
import net.thumbtack.school.notes.dto.response.comment.CreateCommentDtoResponse;
import net.thumbtack.school.notes.dto.response.comment.EditCommentDtoResponse;
import net.thumbtack.school.notes.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentsEndpoint {
    private CommentService commentService;

    @Autowired
    public CommentsEndpoint(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CreateCommentDtoResponse createComment(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                                 @RequestBody CreateCommentDtoRequest req) throws ServerException {

        return commentService.createComment(req, javaSessionId);
    }

    @PutMapping(value = "/{id}")
    public EditCommentDtoResponse editComment(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                              @RequestBody EditCommentDtoRequest req,
                                              @PathVariable("id") int id) throws ServerException {
        return commentService.changeCommentBody(id, req, javaSessionId);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteComment(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                           @PathVariable("id") int id) throws ServerException {
        commentService.deleteComment(id, javaSessionId);
    }

}
