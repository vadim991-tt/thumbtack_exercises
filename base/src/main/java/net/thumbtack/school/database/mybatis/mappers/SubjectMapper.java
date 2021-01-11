package net.thumbtack.school.database.mybatis.mappers;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.Subject;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface SubjectMapper {

    @Insert("INSERT INTO subject ( subjectname) VALUES (#{subject.subjectName})")
    @Options(useGeneratedKeys = true, keyProperty = "subject.id")
    Integer insert(@Param("subject") Subject subject);

    @Select("SELECT * FROM subject WHERE subject.id = #{id}")
    Subject getById(@Param("id") Integer id);

    @Select("SELECT * FROM subject")
    List<Subject> getAll();

    @Update("UPDATE subject SET subjectname = #{subject.subjectName} WHERE id = #{subject.id}")
    void update(@Param("subject") Subject subject);

    @Delete("DELETE FROM subject WHERE id = #{subject.id}")
    void delete(@Param("subject") Subject subject);

    @Delete("DELETE FROM subject")
    void deleteAll();

    @Select("SELECT * FROM subject WHERE id IN (SELECT subjectid FROM subject_groups WHERE groupsid IN" +
            " (SELECT id FROM groups WHERE id = #{group.id}))")
    List<Subject> getByGroup(@Param("group") Group group);
}
