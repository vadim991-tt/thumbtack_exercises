package net.thumbtack.school.database.mybatis.mappers;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.Trainee;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TraineeMapper {

    @Insert("INSERT INTO trainee ( firstname, lastname, rating, groupsid) VALUES ( #{trainee.firstName}, #{trainee.lastName}, #{trainee.rating}, #{group.id})")
    @Options(useGeneratedKeys = true, keyProperty = "trainee.id")
    Integer insert(@Param("trainee") Trainee trainee, @Param("group") Group group);

    @Select("SELECT id, firstname, lastname, rating FROM trainee where id = #{id}")
    Trainee getById(int id);

    @Select("SELECT * FROM trainee")
    List<Trainee> getAll();

    @Update("UPDATE trainee SET firstname = #{trainee.firstName}, lastname = #{trainee.lastName}, rating = #{trainee.rating} WHERE id = #{trainee.id}")
    void update(@Param("trainee") Trainee trainee);

    @Select({"<script>",
            "SELECT * FROM trainee",
            "<where>",
            "<if test='firstname != null'> firstname like #{firstname}",
            "</if>",
            "<if test='lastname != null'> AND lastname like #{lastname}",
            "</if>",
            "<if test='rating != null'> AND rating = #{rating}",
            "</if>",
            "</where>",
            "</script>"})
    List<Trainee> getAllWithParams(@Param("firstname") String firstName, @Param("lastname") String lastName, @Param("rating") Integer rating);

    @Insert({"<script>",
            "INSERT INTO trainee (firstname, lastname, rating) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "( #{item.firstName}, #{item.lastName}, #{item.rating})",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "list.id")
    void batchInsert(@Param("list") List<Trainee> trainees);

    @Delete("DELETE FROM trainee WHERE id = #{trainee.id}")
    void delete(@Param("trainee") Trainee trainee);

    @Delete("DELETE FROM trainee")
    void deleteAll();

    @Select("SELECT * from trainee WHERE groupsid= #{group.id}")
    List<Trainee> getByGroup(@Param("group") Group group);
}

