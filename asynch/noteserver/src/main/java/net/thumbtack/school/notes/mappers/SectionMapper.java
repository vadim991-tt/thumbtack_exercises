package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Section;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface SectionMapper {

    @Insert("INSERT INTO section VALUES (null, #{section.name}, #{authorId})")
    @Options(useGeneratedKeys = true, keyProperty = "section.id")
    void insert(@Param("section") Section section, @Param("authorId") int authorId);

    @Update("UPDATE section SET name = #{name} WHERE id = #{id} AND authorId = #{authorId}")
    void rename(@Param("id") int id, @Param("authorId") int authorId, @Param("name") String name);

    @Select("SELECT * FROM section WHERE id = #{id}")
    Section getInformation(@Param("id") int id);

    @Delete("DELETE FROM section WHERE id = #{id} AND authorId = #{authorId}")
    void delete(@Param("id") int id, @Param("authorId") int authorId);

    @Delete("DELETE FROM section WHERE id = #{id}")
    void deleteBySuper(@Param("id") int id);

    @Select("SELECT * FROM section")
    List<Section> getAll();

    @Select("SELECT * FROM section WHERE authorId = #{author.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "notes", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getBySection", fetchType = FetchType.LAZY))
    })
    List<Section> getByAuthor(@Param("author") Author author);

    @Select("SELECT * FROM section WHERE id = (SELECT sectionId FROM note WHERE id = #{note.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "notes", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getBySection", fetchType = FetchType.LAZY))
    })
    Section getByNote(@Param("note") Note note);


    @Delete("DELETE FROM section")
    void deleteAll();

}
