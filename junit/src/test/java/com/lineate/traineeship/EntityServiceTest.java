package com.lineate.traineeship;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class EntityServiceTest {

    private final static UserService USERVICE = ServiceFactory.createUserService();
    private final static EntityService ESERVICE = ServiceFactory.createEntityService();

    @Test
    public void testCreateEntity() {
        Group adminGroup = USERVICE.createGroup("admin");
        User userBoris = USERVICE.createUser("Boris", adminGroup);

        Entity entity = new Entity("key", "123");

        assertTrue(ESERVICE.save(userBoris, entity));
    }

    @Test
    public void testGetEntityByName() {
        Group adminGroup = USERVICE.createGroup("admin");
        User userBoris = USERVICE.createUser("Boris", adminGroup);
        Entity entity = new Entity("password", "12345");

        ESERVICE.save(userBoris, entity);
        Entity entityFromDB = ESERVICE.getByName(userBoris, "password");

        assertEquals(entity, entityFromDB);
    }

//    @Test
//    public void testDeleteEntity() {
//        Group adminGroup = USERVICE.createGroup("admin");
//        User userBoris = USERVICE.createUser("Boris", adminGroup);
//
//        Entity entity = new Entity("password", "12345");
//        ESERVICE.save(userBoris, entity);
//        Entity entityFromDB = ESERVICE.getByName(userBoris, "password");
//
//        assertEquals(entity, entityFromDB);
//        assertTrue(ESERVICE.delete(userBoris, entity));
//        assertEquals(null, ESERVICE.getByName(userBoris, "password"));
//    }

    @Test
    public void testGrantPermissions() {
        Group adminGroup = USERVICE.createGroup("admin");
        User userBoris = USERVICE.createUser("Boris", adminGroup);

        Entity entity = new Entity("password", "12345");

        ESERVICE.save(userBoris, entity);
        ESERVICE.grantPermission(entity, adminGroup, Permission.write);
    }

    @Test
    public void testGrantReadPermissionsAndDeleteEntity() {
        Group adminGroup = USERVICE.createGroup("admin");
        User userBoris = USERVICE.createUser("Boris", adminGroup);
        Group secondGroup = USERVICE.createGroup("secondGroup");
        User ivan = USERVICE.createUser("Ivan", secondGroup);
        Entity entity = new Entity("passwordSwordFish", "12345");

        ESERVICE.save(userBoris, entity);

        ESERVICE.grantPermission(entity, secondGroup, Permission.read);
        assertFalse(ESERVICE.delete(ivan, entity));

        ESERVICE.grantPermission(entity, secondGroup, Permission.write);
        assertTrue(ESERVICE.delete(ivan, entity));
    }

//    @Test
//    public void testDeleteEntityByGroupMember() {
//        Group adminGroup = USERVICE.createGroup("admin");
//        User userBoris = USERVICE.createUser("Boris", adminGroup);
//
//        User ivan = USERVICE.createUser("Ivan", adminGroup);
//        Entity entity = new Entity("pswrd", "12345");
//
//        assertTrue(ESERVICE.save(userBoris, entity));
//        assertTrue(ESERVICE.delete(ivan, entity));
//    }

//    @Test
//    public void testAccessToEntityFromGroup() {
//        Group adminGroup = USERVICE.createGroup("admin");
//        User userBoris = USERVICE.createUser("Boris", adminGroup);
//
//        User ivan = USERVICE.createUser("Ivan", adminGroup);
//        Entity entity = new Entity("pass", "12345");
//        ESERVICE.save(userBoris, entity);
//        Entity entityFromDB = ESERVICE.getByName(ivan, entity.getName());
//
//        assertEquals(entity, entityFromDB);
//    }

    public static Stream<Arguments> parameters() {
        User userBoris = USERVICE.createUser("Boris");
        return Stream.of(
                Arguments.arguments(null, "1234", userBoris),
//                Arguments.arguments("name", null, userBoris),
                Arguments.arguments("name with spaces", "1234", userBoris),
                Arguments.arguments("________nameThatLargerThan32Characters________",
                        "1234", userBoris),
                Arguments.arguments("", "galaxy", userBoris)
        );
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void testCreateEntityWithIncorrectFields(String name, String value, User boris) {
        Entity entity = new Entity(name, value);
        assertFalse(ESERVICE.save(boris, entity));
    }

}
