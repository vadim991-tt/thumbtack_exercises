package net.thumbtack.school.database.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Group {
    private int id;
    private String groupName;
    private String roomName;
    private List<Trainee> trainees;
    private List<Subject> subjects;

    public Group() {

    }

    public Group(int id, String groupName, String roomName, List<Trainee> trainees, List<Subject> subjects) {
        this.id = id;
        this.groupName = groupName;
        this.roomName = roomName;
        this.trainees = trainees;
        this.subjects = subjects;
    }

    public Group(int id, String groupName, String roomName) {
        this(id, groupName, roomName, new ArrayList<>(), new ArrayList<>());
    }

    public Group(String groupName, String roomName) {
        this(0, groupName, roomName);
    }

    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getRoomName() {
        return roomName;
    }


    public List<Trainee> getTrainees() {
        return trainees;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addTrainee(Trainee trainee) {
        trainees.add(trainee);
    }

    public void removeTrainee(Trainee trainee) {
        trainees.remove(trainee);
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }


    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + groupName + '\'' +
                ", room='" + roomName + '\'' +
                ", trainees=" + trainees +
                ", subjects=" + subjects +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return getId() == group.getId() &&
                Objects.equals(getGroupName(), group.getGroupName()) &&
                Objects.equals(getRoomName(), group.getRoomName()) &&
                Objects.equals(getTrainees(), group.getTrainees()) &&
                Objects.equals(getSubjects(), group.getSubjects());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGroupName(), getRoomName(), getTrainees(), getSubjects());
    }
}
