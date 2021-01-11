package com.lineate.traineeship;

public interface EntityRepository {
    boolean save(Entity entity);

    Entity getByName(String name);

    boolean delete(Entity entity);
}
