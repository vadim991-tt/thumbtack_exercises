package net.thumbtack.school.notes.mapstruct;

import net.thumbtack.school.notes.dto.request.comment.CreateCommentDtoRequest;
import net.thumbtack.school.notes.dto.response.comment.CommentDtoResponse;
import net.thumbtack.school.notes.dto.response.comment.CreateCommentDtoResponse;
import net.thumbtack.school.notes.dto.response.comment.EditCommentDtoResponse;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Comment;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DtoCommentsMapper {

    DtoCommentsMapper INSTANCE = Mappers.getMapper(DtoCommentsMapper.class);

    Comment commentFromCreateDtoReq(CreateCommentDtoRequest request);

    @Mapping(source = "author", target = "authorId", qualifiedByName = "idFromAuthor")
    CreateCommentDtoResponse createDtoRespFromComment(Comment comment);

    @IterableMapping(qualifiedByName = "responseFromComment")
    List<CommentDtoResponse> listCommentDtoFromComments(List<Comment> comments);

    @Named("responseFromComment")
    @Mapping(source = "author", target = "authorId", qualifiedByName = "idFromAuthor")
    CommentDtoResponse commentDtoFromComment(Comment comment);

    @Mapping(source = "author", target = "authorId", qualifiedByName = "idFromAuthor")
    EditCommentDtoResponse editCommentDtoRespFromComment(Comment comment);

    @Named("idFromAuthor")
    static int idFromAuthor(Author author) {
        return author.getId();
    }
}
