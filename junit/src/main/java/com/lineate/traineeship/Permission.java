package com.lineate.traineeship;

public enum Permission {
    read(0), write(1);

    private final int level;

    Permission(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
