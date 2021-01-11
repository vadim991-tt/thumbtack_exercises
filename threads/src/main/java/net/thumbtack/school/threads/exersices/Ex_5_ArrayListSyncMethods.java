package net.thumbtack.school.threads.exersices;

import net.thumbtack.school.threads.model.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ArrayThread extends Thread {
    private final Random random = new Random();
    private final List<Integer> list;
    private final Action action;

    public ArrayThread(List<Integer> list, Action action) {
        this.list = list;
        this.action = action;
    }

    public void run() {
        if (action.equals(Action.INCREASE)) {
            increase(action);
        } else {
            reduce(action);
        }
    }

    public void increase(Action action) {
        for (int countOperation = 0; countOperation < 10000; countOperation++) {
            doOperation(action);
        }
        System.out.println("Exiting append thread");
    }

    public void reduce(Action action) {
        for (int countOperations = 0; countOperations < 10000; countOperations++) {
            doOperation(action);
        }
        System.out.println("Exiting reduce thread");
    }


    public synchronized void doOperation(Action action) {

        if (action.equals(Action.INCREASE)) {
            int randomInt = random.nextInt(10000);
            list.add(randomInt);
        } else {
            if (!list.isEmpty()) {
                int randomInt = random.nextInt(list.size());
                list.remove(randomInt);
            }
        }

    }
}

public class Ex_5_ArrayListSyncMethods {
    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>(10000);
        ArrayThread appender = new ArrayThread(list, Action.INCREASE);
        ArrayThread reducer = new ArrayThread(list, Action.REDUCE);

        appender.start();
        reducer.start();

        try {
            appender.join();
            reducer.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
        System.out.printf("Current list size: %s %n", list.size());


    }
}
