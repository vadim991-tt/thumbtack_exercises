package com.lineate.traineeship;

public class Entity {
    private final String name;
    private String value;

    public Entity(String name) {
        this.name = name;
    }

    public Entity(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Entity(Entity entity) {
        name = entity.name;
        value = entity.value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }

        Entity entity = (Entity) o;

        return name.equals(entity.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
