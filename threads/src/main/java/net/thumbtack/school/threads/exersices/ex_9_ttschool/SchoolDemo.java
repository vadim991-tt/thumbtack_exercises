package net.thumbtack.school.threads.exersices.ex_9_ttschool;


import net.thumbtack.school.threads.model.Operation;
import net.thumbtack.school.threads.ttschool.Group;
import net.thumbtack.school.threads.ttschool.School;
import net.thumbtack.school.threads.ttschool.TrainingException;

import java.util.Set;

class SchoolThread extends Thread {
    private static School school;
    private final Operation op;

    public SchoolThread(School school, Operation op) {
        SchoolThread.school = school;
        this.op = op;
    }

    public static void addGroups() {
        System.out.println("Adding 100 groups by Thread: " + Thread.currentThread().getName());
        for (int i = 100; i < 200; i++) {
            synchronized (school) {
                try {
                    school.addGroup(new Group(String.format("Group%s", i), "room"));
                    System.out.printf("Group%s was added successfully%n", i);
                    Thread.sleep(200);
                } catch (TrainingException | InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }
    }


    public static void removeGroups() {
        System.out.println("Removing 100 groups by Thread: " + Thread.currentThread().getName());
        for (int i = 0; i < 100; i++) {
            synchronized (school) {
                try {
                    school.removeGroup(String.format("Group%s", i));
                    System.out.printf("Group%s was deleted successfully%n", i);
                    Thread.sleep(200);
                } catch (TrainingException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void run() {
        if (op.equals(Operation.ADD)) {
            addGroups();
        } else {
            removeGroups();
        }
    }
}


public class SchoolDemo {
    public static void main(String[] args) throws TrainingException {
        School school = new School("Thumbtack", 1999);
        fillSchoolWithGroups(school); // to avoid Training Exception (GROUP_NOT_FOUND)

        SchoolThread addGroups = new SchoolThread(school, Operation.ADD);
        SchoolThread removeGroups = new SchoolThread(school, Operation.REMOVE);

        addGroups.start();
        removeGroups.start();

        try {
            addGroups.join();
            removeGroups.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void fillSchoolWithGroups(School school){
        Set<Group> set = school.getGroups();
        for (int i = 0; i < 100; i++) {
            try {
                set.add(new Group(String.format("Group%s", i), "ROOM"));
            } catch (TrainingException e) {
                e.printStackTrace();
            }
        }
    }
}
