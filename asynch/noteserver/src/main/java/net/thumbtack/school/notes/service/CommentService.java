package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.dao.CommentDao;
import net.thumbtack.school.notes.dto.request.comment.CreateCommentDtoRequest;
import net.thumbtack.school.notes.dto.request.comment.EditCommentDtoRequest;
import net.thumbtack.school.notes.dto.response.comment.CommentDtoResponse;
import net.thumbtack.school.notes.dto.response.comment.CreateCommentDtoResponse;
import net.thumbtack.school.notes.dto.response.comment.EditCommentDtoResponse;
import net.thumbtack.school.notes.mapstruct.DtoCommentsMapper;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commentService")
public class CommentService {

    private final CommentDao commentDao;
    private final AuthorDao authorDao;

    @Autowired
    public CommentService(CommentDao commentDao, AuthorDao authorDao) {
        this.commentDao = commentDao;
        this.authorDao = authorDao;
    }

    public CreateCommentDtoResponse createComment(CreateCommentDtoRequest req, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        int noteId = req.getNoteId();
        Comment commentToDB = DtoCommentsMapper.INSTANCE.commentFromCreateDtoReq(req);
        Comment commentFromDB = commentDao.insertComment(author, commentToDB, noteId);
        return DtoCommentsMapper.INSTANCE.createDtoRespFromComment(commentFromDB);
    }


    public List<CommentDtoResponse> getComments(int noteId, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        List<Comment> comments = commentDao.getComments(author, noteId);
        return DtoCommentsMapper.INSTANCE.listCommentDtoFromComments(comments);

    }

    public EditCommentDtoResponse changeCommentBody(int id, EditCommentDtoRequest req, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        Comment comment = commentDao.changeBody(author, id, req.getBody());
        return DtoCommentsMapper.INSTANCE.editCommentDtoRespFromComment(comment);
    }

    public void deleteComment(int id, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        commentDao.delete(author, id);
    }

    public void deleteCommentsFromNote(int noteId, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author author = authorDao.getAuthorByUUID(uuid);
        commentDao.deleteFromNote(author, noteId);
    }


}
