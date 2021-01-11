package com.lineate.traineeship;

public interface EntityService {
    boolean save(User user, Entity entity) throws IllegalArgumentException;

    Entity getByName(User user, String name);

    boolean delete(User user, Entity entity);

    void grantPermission(Entity entity, Group group, Permission permission);
}
