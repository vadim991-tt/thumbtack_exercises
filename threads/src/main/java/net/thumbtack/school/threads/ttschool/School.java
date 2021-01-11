package net.thumbtack.school.threads.ttschool;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class School {
    private String name;
    private int year;
    private Set<Group> set;

    public School(String name, int year) throws TrainingException {
        setName(name);
        setYear(year);
        this.set = Collections.synchronizedSet(new HashSet<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws TrainingException {
        if (name == null || name.length() == 0) {
            throw new TrainingException(TrainingErrorCode.SCHOOL_WRONG_NAME, name);
        }
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Set<Group> getGroups() {
        return set;
    }

    public void addGroup(Group group) throws TrainingException {
        for (Group elem : set) {
            if (elem.getName().equals(group.getName())) {
                throw new TrainingException(TrainingErrorCode.DUPLICATE_GROUP_NAME);
            }
        }
        set.add(group);
    }

    public void removeGroup(Group group) throws TrainingException {
        if (!set.remove(group))
            throw new TrainingException(TrainingErrorCode.GROUP_NOT_FOUND);
    }

    public void removeGroup(String name) throws TrainingException {
        if (!set.removeIf(elem -> (elem.getName().equals(name)))) {
            throw new TrainingException(TrainingErrorCode.GROUP_NOT_FOUND);
        }
    }

    public boolean containsGroup(Group group) {
        return set.contains(group);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return year == school.year &&
                Objects.equals(name, school.name) &&
                Objects.equals(set, school.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year, set);
    }
}

