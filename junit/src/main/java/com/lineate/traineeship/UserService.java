package com.lineate.traineeship;

public interface UserService {
    User createUser(String name);

    User createUser(String name, Group group);

    Group createGroup(String name);

    void addUserToGroup(User user, Group group);
}
