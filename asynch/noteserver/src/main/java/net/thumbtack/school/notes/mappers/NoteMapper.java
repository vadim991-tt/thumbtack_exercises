package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Revision;
import net.thumbtack.school.notes.model.Section;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO note  VALUES(null, #{note.subject}, #{note.body}, 0, 0, 0, #{sectionId}, #{authorId}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "note.id")
    void insert(@Param("note") Note note, @Param("sectionId") int id, @Param("authorId") int authorId);

    @Insert("INSERT INTO revision VALUES (null, #{noteId}, #{body}, now())")
    void insertRevision(@Param("noteId") int noteId, @Param("body") String body);

    @Update("UPDATE note SET body = #{body} WHERE id = #{id} AND authorId = #{authorId}")
    void changeBody(@Param("id") int id, @Param("body") String body, @Param("authorId") int authorId);

    @Update("UPDATE note SET sectionId = #{sectionId} WHERE id = #{id} AND authorId = #{authorId}")
    void move(@Param("id") int id, @Param("sectionId") int SectionId, @Param("authorId") int authorId);

    @Update("UPDATE note SET sectionId = #{sectionId} WHERE id = #{id}")
    void moveBySuper(@Param("id") int id, @Param("sectionId") int sectionId);

    @Update("UPDATE note SET body = #{body}, sectionId = #{sectionId} WHERE id = #{id} AND authorId = #{authorId}")
    void moveAndChangeBody(@Param("id") int id, @Param("sectionId") int sectionId, @Param("body") String body, @Param("authorId") int authorId);

    @Update("UPDATE note SET rating = rating + #{rating}, rates = rates + 1, averageRating = rating / rates WHERE id = #{id}")
    void countNoteRating(@Param("id") int id, @Param("rating") int rating);

    @Update("UPDATE note SET rating = rating - #{previousRating} + #{rating}, averageRating = rating / rates WHERE id = #{id}")
    void changeNoteRating(@Param("id") int id, @Param("rating") int rating, @Param("previousRating") int previousRating);

    @Delete("DELETE FROM note WHERE id = #{id}")
    void deleteBySuper(@Param("id") int id);

    @Delete("DELETE FROM note WHERE id = #{id} AND authorId = #{authorId}")
    void delete(@Param("id") int id, @Param("authorId") int authorId);

    @Delete("DELETE FROM note")
    void deleteAll();

    @Select("SELECT `value` FROM rating WHERE noteId = #{id} AND authorId = #{authorId}")
    Integer getPreviousRating(@Param("id") int id, @Param("authorId") int authorId);

    @Insert("REPLACE INTO rating VALUES (null, #{id}, #{authorId}, #{value})")
    void rateNote(@Param("id") int id, @Param("authorId") int authorId, @Param("value") int value);

    @Select("SELECT id, `subject`, body, averageRating, created FROM note WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "rating", column = "averageRating"),
            @Result(property = "revisions", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getByNote", fetchType = FetchType.LAZY)),
            @Result(property = "author", column = "id",
                    one = @One(select = "net.thumbtack.school.notes" +
                            ".mappers.AuthorMapper.getByNote")),
            @Result(property = "section", column = "id",
                    one = @One(select = "net.thumbtack.school.notes" +
                            ".mappers.SectionMapper.getByNote"))
    })
    Note getNoteByID(@Param("id") int id);

    @Select("SELECT id, `subject`, body, averageRating, created FROM note WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "rating", column = "averageRating"),
            @Result(property = "revisions", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getByNote", fetchType = FetchType.LAZY)),
            @Result(property = "author", column = "id",
                    one = @One(select = "net.thumbtack.school.notes" +
                            ".mappers.AuthorMapper.getByNote")),
            @Result(property = "section", column = "id",
                    one = @One(select = "net.thumbtack.school.notes" +
                            ".mappers.SectionMapper.getByNote"))
    })
    Note getLatestVersion(@Param("id") int id);

    @Select("SELECT max(id), noteId, body, created  FROM revision WHERE noteId = #{note.id}")
    @Results({
            @Result(property = "max(id)", column = "id"),
            @Result(property = "comments", column = "max(id)", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.CommentMapper.getByRevision", fetchType = FetchType.LAZY))

    })
    List<Revision> getLatestRevision(@Param("note") Note note);

    @Select("SELECT * FROM revision WHERE noteId = #{note.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "comments", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.CommentMapper.getByRevision", fetchType = FetchType.LAZY))

    })
    List<Revision> getByNote(@Param("note") Note note);

    @Select("SELECT * FROM note WHERE authorId = #{author.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "revisions", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getByNote", fetchType = FetchType.LAZY)),
            @Result(property = "author", column = "id",
                    one = @One(select = "net.thumbtack.school.notes" +
                            ".mappers.AuthorMapper.getByNote", fetchType = FetchType.LAZY)),
            @Result(property = "section", column = "id",
                    one = @One(select = "net.thumbtack.school.notes" +
                            ".mappers.SectionMapper.getByNote", fetchType = FetchType.LAZY))
    })
    List<Note> getByAuthor(@Param("author") Author author);


    @Select("SELECT * FROM note WHERE sectionId = #{section.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "revisions", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getByNote", fetchType = FetchType.LAZY)),
            @Result(property = "author", column = "id",
                    one = @One(select = "net.thumbtack.school.notes" +
                            ".mappers.AuthorMapper.getByNote", fetchType = FetchType.LAZY)),
            @Result(property = "section", column = "id",
                    one = @One(select = "net.thumbtack.school.notes" +
                            ".mappers.SectionMapper.getByNote", fetchType = FetchType.LAZY))
    })
    List<Note> getBySection(@Param("section") Section section);

    @SelectProvider(method = "selectNoteLike", type = net.thumbtack.school.notes.dao.providers.NoteDAOProvider.class)
    @ResultMap("selectNotes")
    List<Note> getUsingSQLBuilder(@Param("id") String id,
                                  @Param("sectionId") String sectionId,
                                  @Param("sortByRating") String sortByRating,
                                  @Param("tags") String tags,
                                  @Param("allTags") String allTags,
                                  @Param("timeFrom") String timeFrom,
                                  @Param("timeTo") String timeTo,
                                  @Param("user") String user,
                                  @Param("include") String include,
                                  @Param("commentVersion") String commentVersion,
                                  @Param("from") String from,
                                  @Param("count") String count);

}
