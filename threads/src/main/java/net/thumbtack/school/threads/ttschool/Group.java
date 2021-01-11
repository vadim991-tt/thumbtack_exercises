package net.thumbtack.school.threads.ttschool;


import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group {
    private String name;
    private String room;
    private List<Trainee> list;

    public Group(String name, String room) throws TrainingException {
        setName(name);
        setRoom(room);
        this.list = Collections.synchronizedList(new ArrayList<Trainee>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws TrainingException {
        if (name == null || name.length() == 0) {
            throw new TrainingException(TrainingErrorCode.GROUP_WRONG_NAME, name);
        }
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) throws TrainingException {
        if (room == null || room.length() == 0) {
            throw new TrainingException(TrainingErrorCode.GROUP_WRONG_ROOM, room);
        }
        this.room = room;
    }

    public List<Trainee> getTrainees() {
        return list;
    }

    public void addTrainee(Trainee trainee) {
        list.add(trainee);
    }

    public void removeTrainee(Trainee trainee) throws TrainingException {
        if (!list.remove(trainee)) {
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
    }

    public void removeTrainee(int index) throws TrainingException {
        try {
            list.remove(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
    }

    public Trainee getTraineeByFirstName(String firstName) throws TrainingException {
        for (Trainee trainee : list) {
            if (trainee.getFirstName().equals(firstName)) {
                return trainee;
            }
        }
        throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public Trainee getTraineeByFullName(String fullName) throws TrainingException {
        for (Trainee trainee : list) {
            if (trainee.getFullName().equals(fullName)) {
                return trainee;
            }
        }
        throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public void sortTraineeListByFirstNameAscendant() {
        list.sort((Trainee o1, Trainee o2) -> o1.getFirstName().compareTo(o2.getFirstName()));
    }

    public void sortTraineeListByRatingDescendant() {
        list.sort(Comparator.comparing(Trainee::getRating).reversed());
    }

    public void reverseTraineeList() {
        Collections.reverse(list);
    }

    public void rotateTraineeList(int positions) {
        Collections.rotate(list, positions);
    }

    public List<Trainee> getTraineesWithMaxRating() throws TrainingException {
        if (list.size() == 0) {
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
        Trainee trainee = Collections.max(list, Comparator.comparing(Trainee::getRating));
        int max = trainee.getRating();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getRating() != max) {
                list.remove(i);
                i--;
            }
        }
        return list;
    }

    public boolean hasDuplicates() {
        Set<Trainee> traineeSet = new HashSet<>(list);
        return traineeSet.size() != list.size();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(name, group.name) &&
                Objects.equals(room, group.room) &&
                Objects.equals(list, group.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, room, list);
    }
}