package com.lineate.traineeship;

public class UserServiceImpl implements UserService {
    @Override
    public User createUser(String name) {
        final Group group = createGroup(name);
        final User user = new User(name, group);
        group.addUser(user);
        return user;
    }

    @Override
    public User createUser(String name, Group group) {
        final User user = new User(name, group);
        group.addUser(user);
        return user;
    }

    @Override
    public Group createGroup(String name) {
        return new Group(name);
    }

    @Override
    public void addUserToGroup(User user, Group group) {
        group.addUser(user);
    }
}
