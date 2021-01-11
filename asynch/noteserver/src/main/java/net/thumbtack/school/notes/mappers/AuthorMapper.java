package net.thumbtack.school.notes.mappers;

import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Role;
import net.thumbtack.school.notes.view.AuthorView;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;


@Mapper
public interface AuthorMapper {

    @Insert("INSERT INTO author VALUES (null, #{author.firstName}, #{author.lastName}, #{author.patronymic}, #{author.login}, #{author.password}, 0, 0, now(), 'REGULAR', false)")
    @Options(useGeneratedKeys = true, keyProperty = "author.id")
    void insert(@Param("author") Author author);

    @Insert("INSERT INTO `session` VALUES (null, (SELECT id FROM author WHERE login = #{login} AND password = #{password} AND isDeleted = false), #{uuid}, now())")
    void login(@Param("login") String login, @Param("password") String password, @Param("uuid") String uuid);

    @Insert("INSERT INTO `session` VALUES (null, #{id}, #{uuid}, now())")
    void loginById(@Param("id") int id, @Param("uuid") String uuid);

    @Delete("DELETE FROM `session` WHERE uuid = #{uuid}")
    void logout(@Param("uuid") String uuid);

    @Update("UPDATE author SET isDeleted = true WHERE id=(#{id}) AND password = #{password}")
    void deleteAuthor(@Param("password") String password, @Param("id") int id);

    @Update("UPDATE author SET firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic}, password = #{newPassword} WHERE id = #{id} AND password = #{oldPassword}")
    void changeUserInfoWithPatronymic(@Param("id") int id, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("patronymic") String patronymic, @Param("newPassword") String newPassword, @Param("oldPassword") String oldPassword);

    @Update("UPDATE author SET firstName = #{firstName}, lastName = #{lastName} , password = #{newPassword} WHERE id = #{id} AND password = #{oldPassword}")
    void changeUserInfo(@Param("id") int id, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

    @Update("UPDATE author SET createdNotes = createdNotes + 1 WHERE id = #{id}")
    void increaseAuthorNotesQuantity(@Param("id") int id);

    @Update("UPDATE author SET createdNotes = createdNotes - 1 WHERE id = #{id}")
    void reduceAuthorNotesQuantity(@Param("id") int id);

    @Update("UPDATE author SET role = 'SUPER' WHERE id = #{id}")
    void addUserToSuper(@Param("id") int id);

    // TODO: Do something with this large ugly sql query
    @Update("UPDATE author SET rating = (Select SUM(averageRating) as rating_sum FROM note WHERE authorId = (SELECT authorId FROM note WHERE note.id = #{noteId})) / createdNotes WHERE id = (SELECT authorId FROM note WHERE note.id = #{noteId})")
    void countAuthorRating(@Param("noteId") int noteId);

    @Select("SELECT role FROM author WHERE id = (SELECT authorId FROM `session` WHERE uuid = #{uuid})")
    Role checkIfUserIsSuper(@Param("uuid") String uuid);

    @Insert("INSERT INTO followed VALUES (null, #{id}, (SELECT id FROM author WHERE login = #{login}))")
    void followUser(@Param("id") int id, @Param("login") String login);

    @Insert("INSERT INTO ignored VALUES (null, #{id}, (SELECT id FROM author WHERE login = #{login}))")
    void ignoreUser(@Param("id") int id, @Param("login") String login);

    @Delete("DELETE FROM followed WHERE authorId = #{id} AND followedId = (SELECT id FROM author WHERE login = #{login})")
    void unfollowUser(@Param("id") int id, @Param("login") String login);

    @Delete("DELETE FROM ignored WHERE authorId = #{id} AND ignoredId = (SELECT id FROM author WHERE login = #{login})")
    void stopIgnoringUser(@Param("id") int id, @Param("login") String login);

    @Select("SELECT author.id, firstname, lastname, patronymic, login, timeRegistered, uuid, isDeleted, role, rating FROM author INNER JOIN `session` ON author.id = authorId;")
    List<AuthorView> getAuthors();

    @Select("SELECT * FROM author WHERE id = (SELECT authorId FROM note WHERE id = #{note.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "notes", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getByAuthor", fetchType = FetchType.LAZY)),
            @Result(property = "sections", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.SectionMapper.getByAuthor", fetchType = FetchType.LAZY))
    })
    Author getByNote(@Param("note") Note note);

    @Select("SELECT * FROM author WHERE id=(SELECT authorId FROM `session` WHERE uuid = #{uuid})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "notes", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getByAuthor", fetchType = FetchType.LAZY)),
            @Result(property = "sections", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.SectionMapper.getByAuthor", fetchType = FetchType.LAZY))
    })
    Author getByUUID(String uuid);


    @Select("SELECT * FROM author WHERE id = (SELECT authorId FROM `comment` WHERE id = #{comment.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "notes", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getByAuthor", fetchType = FetchType.LAZY)),
            @Result(property = "sections", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.SectionMapper.getByAuthor", fetchType = FetchType.LAZY))
    })
    Author getByComment(@Param("comment") Comment comment);

    @Select("SELECT * FROM author WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "notes", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.NoteMapper.getByAuthor", fetchType = FetchType.LAZY)),
            @Result(property = "sections", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.notes" +
                            ".mappers.SectionMapper.getByAuthor", fetchType = FetchType.LAZY))
    })
    Author getById(@Param("id") int id);


    @SelectProvider(method = "selectAuthorLike", type = net.thumbtack.school.notes.dao.providers.AuthorDAOProvider.class)
    List<AuthorView> getUsingSQLBuilder(@Param("id") String id,
                                        @Param("superUserRequest") String superUserRequestCondition,
                                        @Param("sortByRating") String sortByRating,
                                        @Param("type") String typeCondition,
                                        @Param("count") String countCondition,
                                        @Param("from") String fromCondition);


    @Select("SELECT authorId FROM `session` WHERE uuid = #{uuid}")
    Integer geIdByUuid(@Param("uuid") String uuid);

    @Select("SELECT created FROM `session` WHERE uuid = #{uuid}")
    Timestamp getSessionTime(@Param("uuid") String uuid);

    @Update("UPDATE `session` SET created = now() WHERE uuid = #{uuid}")
    void updateSessionTime(@Param("uuid") String uuid);

    // Test methods
    @Delete("DELETE FROM author")
    void deleteAll();

    @Insert("INSERT INTO author VALUES (null, #{author.firstName}, #{author.lastName}, #{author.patronymic}, #{author.login}, #{author.password}, 0, 0, now(), 'SUPER', false)")
    @Options(useGeneratedKeys = true, keyProperty = "author.id")
    void insertAdmin(@Param("author") Author author);

}
