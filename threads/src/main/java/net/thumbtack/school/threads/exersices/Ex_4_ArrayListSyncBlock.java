package net.thumbtack.school.threads.exersices;


import java.util.ArrayList;
import java.util.Random;


class Appender extends Thread {
    private final Random random;
    private final ArrayList<Integer> list;

    public Appender(ArrayList<Integer> list, Random random) {
        this.list = list;
        this.random = random;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            synchronized (list) {
                int randomInt = random.nextInt(10000);
                list.add(randomInt);
            }
        }
        System.out.println("Exiting appender thread");
    }
}

class Reducer extends Thread {
    private final Random random;
    private final ArrayList<Integer> list;

    public Reducer(ArrayList<Integer> list, Random random) {
        this.list = list;
        this.random = random;
    }

    @Override
    public void run() {
        int countOperations = 0;
        while (countOperations < 10000) {
            synchronized (list) {
                if (!list.isEmpty()) {
                    int randomInt = random.nextInt(list.size());
                    list.remove(randomInt);
                    countOperations++;
                }
            }
        }
        System.out.println("Exiting reducer thread");
    }
}

public class Ex_4_ArrayListSyncBlock {

    public static void main(String[] args) {
        Random random = new Random();
        ArrayList<Integer> list = new ArrayList<>(100000);
        System.out.println();
        Appender appender = new Appender(list, random);
        Reducer reducer = new Reducer(list, random);

        appender.start();
        reducer.start();


        try {
            appender.join();
            reducer.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
        System.out.printf("Current list size: %s %n", list.size());
        System.out.println("Exiting main thread");

    }

}

