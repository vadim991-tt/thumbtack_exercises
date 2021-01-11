package net.thumbtack.school.database.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class School {
    private int id;
    private String schoolName;
    private int year;
    private List<Group> groups;

    public School() {

    }

    public School(int id, String schoolName, int year, List<Group> groups) {
        this.id = id;
        this.schoolName = schoolName;
        this.year = year;
        this.groups = groups;
    }

    public School(int id, String schoolName, int year) {
        this(id, schoolName, year, new ArrayList<>());
    }

    public School(String schoolName, int year) {
        this(0, schoolName, year);
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + schoolName + '\'' +
                ", year=" + year +
                ", groups=" + groups +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof School)) return false;
        School school = (School) o;
        return getId() == school.getId() &&
                getYear() == school.getYear() &&
                Objects.equals(getSchoolName(), school.getSchoolName()) &&
                Objects.equals(getGroups(), school.getGroups());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSchoolName(), getYear(), getGroups());
    }
}
