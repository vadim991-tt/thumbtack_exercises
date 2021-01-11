package net.thumbtack.school.threads.ttschool;

import java.util.*;

public class TraineeMap {
    private Map<Trainee, String> map;

    public TraineeMap() {
        this.map = new HashMap<>();
    }

    public void addTraineeInfo(Trainee trainee, String institute) throws TrainingException {
        if (map.put(trainee, institute) != null)
            throw new TrainingException(TrainingErrorCode.DUPLICATE_TRAINEE);
    }

    public void replaceTraineeInfo(Trainee trainee, String institute) throws TrainingException {
        if (map.replace(trainee, institute) == null)
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public void removeTraineeInfo(Trainee trainee) throws TrainingException {
        if (map.remove(trainee) == null)
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public int getTraineesCount() {
        return map.size();
    }

    public String getInstituteByTrainee(Trainee trainee) throws TrainingException {
        if (map.get(trainee) == null) {
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
        return map.get(trainee);
    }

    public Set<Trainee> getAllTrainees() {
        return map.keySet();
    }

    public Set<String> getAllInstitutes() {
        return new HashSet<>(map.values());
    }

    public boolean isAnyFromInstitute(String institute) {
        for (Trainee key : map.keySet())
            if (map.get(key).equals(institute))
                return true;
        return false;
    }
}
