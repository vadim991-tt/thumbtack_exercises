package com.lineate.traineeship;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;


import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {

    private static final UserService USERVICE = ServiceFactory.createUserService();

    @Test
    public void testCreateUserWithDefaultGroup() {
        User boris = USERVICE.createUser("Boris");

        SoftAssertions.assertSoftly(softy -> {
            softy.assertThat(boris).isNotNull()
                    .extracting(User::getName).isEqualTo("Boris");
            softy.assertThat(boris.getGroups())
                    .containsOnly(new Group("Boris"));
        });
    }

    @Test
    public void testCreateUserWithGroup() {
        Group friends = USERVICE.createGroup("Friends");
        User vasya = USERVICE.createUser("Vasya", friends);

        assertAll(
                () -> assertEquals("Vasya", vasya.getName()),
                () -> assertEquals("Friends", friends.getName()),
                () -> assertTrue(vasya.getGroups().contains(friends)),
                () -> assertTrue(friends.getUsers().contains(vasya))
        );
    }

    @Test
    public void testAddUserToGroup() {
        Group group = USERVICE.createGroup("Enemies");
        User user = USERVICE.createUser("user");

        USERVICE.addUserToGroup(user, group);

        assertTrue(user.getGroups().contains(group));
        assertTrue(group.getUsers().contains(user));
    }

    @Test(expected = Exception.class)
    public void testCreateUserWithNullName() {
        User user = USERVICE.createUser(null);
    }

//    @Test(expected = Exception.class)
//    public void testCreateUserWithBlankName() {
//        User user = USERVICE.createUser("");
//    }
//
//    @Test(expected = Exception.class)
//    public void testCreateGroupWithNullName() {
//        Group group = USERVICE.createGroup(null);
//    }
//
//    @Test(expected = Exception.class)
//    public void testCreateGroupWithBlankName() {
//        Group group = USERVICE.createGroup("");
//    }
}
