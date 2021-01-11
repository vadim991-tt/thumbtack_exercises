package com.lineate.traineeship;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseEntityService implements EntityService {
    private final Map<String, User> owners = new HashMap<>();
    private final Map<String, Map<Group, Permission>> authority = new HashMap<>();

    @Override
    public boolean save(User user, Entity entity) throws IllegalArgumentException {
        final String name = entity.getName();
        if (!hasAccess(user, name, Permission.write)) {
            return false;
        }
        if (StringUtils.isEmpty(name)
                || name.contains(" ")
                || name.length() > 32) {
            return false;
        }
        if (getByName(name) == null) {
            owners.put(name, user);
        }
        return save(new Entity(entity));
    }

    @Override
    public Entity getByName(User user, String name) {
        return hasAccess(user, name, Permission.read) ? new Entity(getByName(name)) : null;
    }

    @Override
    public boolean delete(User user, Entity entity) {
        if (hasAccess(user, entity.getName(), Permission.write)) {
            return delete(entity);
        }
        return false;
    }

    @Override
    public void grantPermission(Entity entity, Group group, Permission permission) {
        final Map<Group, Permission> permissions = authority.computeIfAbsent(entity.getName(), n -> new HashMap<>());
        permissions.put(group, permission);
    }

    protected abstract Entity getByName(String name);

    protected abstract boolean save(Entity entity);

    protected abstract boolean delete(Entity entity);

    private boolean hasAccess(final User user, String name, Permission permission) {
        if (getByName(name) == null) {
            return true;
        }
        if (owners.get(name).equals(user)) {
            return true;
        }
        final Map<Group, Permission> entityPermissions = authority.getOrDefault(name, Collections.emptyMap());
        return user.getGroups().stream()
                .map(entityPermissions::get)
                .filter(Objects::nonNull)
                .anyMatch(p -> p.getLevel() >= permission.getLevel());
    }
}
