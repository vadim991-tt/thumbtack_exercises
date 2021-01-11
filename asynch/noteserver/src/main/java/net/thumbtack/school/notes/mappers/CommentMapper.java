package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.model.Revision;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO `comment` VALUES (null, #{comment.body}, #{authorId}, #{noteId}, (SELECT max(id) FROM revision WHERE noteId = #{noteId}), now())")
    @Options(useGeneratedKeys = true, keyProperty = "comment.id")
    void insert(@Param("comment") Comment comment, @Param("authorId") int authorId, @Param("noteId") int id);

    @Select("SELECT * FROM comment WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "id",
                    one = @One(select = "net.thumbtack.school.notes.mappers.AuthorMapper.getByComment", fetchType = FetchType.LAZY))
    })
    Comment getById(@Param("id") int id);

    @Select("SELECT `comment`.id, `comment`.body, noteId, authorId, revisionId FROM `comment` WHERE noteId = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "id",
                    one = @One(select = "net.thumbtack.school.notes.mappers.AuthorMapper.getByComment", fetchType = FetchType.LAZY))
    })
    List<Comment> getByNoteId(@Param("id") int id);

    @Select("SELECT * FROM `comment` WHERE revisionId = #{revision.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "id",
                    one = @One(select = "net.thumbtack.school.notes.mappers.AuthorMapper.getByComment", fetchType = FetchType.LAZY))
    })
    List<Comment> getByRevision(@Param("revision") Revision revision);

    @Update("UPDATE `comment` SET body = #{body} WHERE id = #{id} AND authorId = #{authorId}")
    void changeBody(@Param("id") int id, @Param("body") String body, @Param("authorId") int authorId);

    @Delete("DELETE FROM `comment` WHERE id = #{id} AND authorId = #{authorId}")
    void delete(@Param("id") int id, @Param("authorId") int authorId);

    @Delete("DELETE FROM `comment` WHERE noteId = (SELECT noteId FROM note WHERE authorId = #{authorId} AND noteId = #{noteId})")
    void deleteFromNote(@Param("noteId") int noteId, @Param("authorId") int authorId); // TODO: update this String

}
