package com.lineate.traineeship;

import java.util.Collection;
import java.util.HashSet;

public class Group {
    private final String name;
    private final Collection<User> users = new HashSet<>();

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Collection<User> getUsers() {
        return new HashSet<>(users);
    }

    public void addUser(User user) {
        users.add(user);
        user.getGroups().add(this);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }

        Group group = (Group) o;

        return name.equals(group.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
