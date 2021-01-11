package com.lineate.traineeship;

import java.util.HashMap;
import java.util.Map;

public class InMemoryEntityService extends BaseEntityService {
    private final Map<String, Entity> entities = new HashMap<>();

    @Override
    protected Entity getByName(String name) {
        return entities.get(name);
    }

    @Override
    protected boolean save(Entity entity) {
        entities.put(entity.getName(), entity);
        return entities.containsKey(entity.getName());
    }

    @Override
    protected boolean delete(Entity entity) {
        entities.remove(entity.getName());
        return !entities.containsKey(entity.getName());
    }
}
