package net.thumbtack.school.threads.exersices.ex_9_ttschool;


import net.thumbtack.school.threads.model.Operation;
import net.thumbtack.school.threads.ttschool.Group;
import net.thumbtack.school.threads.ttschool.Trainee;
import net.thumbtack.school.threads.ttschool.TrainingException;

import java.util.List;


class GroupTread extends Thread {
    private final Group group;
    private final Operation op;

    public GroupTread(Group group, Operation op) {
        this.group = group;
        this.op = op;
    }

    public void addTrainees() {
        for (int i = 200; i < 300; i++) {
            try {
                String name = String.format("IVAN №%s", i);
                group.addTrainee(new Trainee(name, "Ivanov", 5));
                System.out.printf("Trainee %s was added successfully%n", name);
                Thread.sleep(200);
            } catch (TrainingException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void removeTrainees() {
        for (int i = 0; i < 100; i++) {
            try {
                group.removeTrainee(i);
                System.out.printf("Trainee with %s index was removed successfully%n", i);
                Thread.sleep(200);
            } catch (TrainingException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public void run() {
        if (op.equals(Operation.ADD)) {
            addTrainees();
        } else {
            removeTrainees();
        }
    }
}

public class GroupDemo {
    public static void main(String[] args) throws TrainingException {
        Group group = new Group("GroupName", "Room");
        fillGroupWithTrainees(group); // to avoid Index out of bounds ex

        GroupTread addTrainees = new GroupTread(group, Operation.ADD);
        GroupTread removeTrainees = new GroupTread(group, Operation.REMOVE);

        addTrainees.start();
        removeTrainees.start();

        try {
            addTrainees.join();
            removeTrainees.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void fillGroupWithTrainees(Group group) {
        List<Trainee> trainees = group.getTrainees();
        for (int i = 0; i < 200; i++) {
            try {
                trainees.add(new Trainee(String.format("ANDREY №%s", i), "Lastname", 5));
            } catch (TrainingException e) {
                e.printStackTrace();
            }
        }
    }

}