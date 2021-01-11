package net.thumbtack.school.threads.exersices;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


class AppenderWithoutSync extends Thread {
    private final List<Integer> list;
    private final Random random;

    public AppenderWithoutSync(List<Integer> list, Random random) {
        this.list = list;
        this.random = random;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            int randomInt = random.nextInt(10000);
            list.add(randomInt);
        }
        System.out.println("Exiting appender thread");
    }
}

class ReducerWithoutSync extends Thread {
    private final List<Integer> list;
    private final Random random;

    public ReducerWithoutSync(List<Integer> list, Random random) {
        this.list = list;
        this.random = random;

    }

    @Override
    public void run() {
        int countOperations = 0;
        while (countOperations < 10000) {
            if (!list.isEmpty()) {
                int randomInt = random.nextInt(list.size());
                list.remove(randomInt);
                countOperations++;
            }
        }
        System.out.println("Exiting reducer thread");
    }
}

public class Ex_6_ArrayListSynchCollections {

    public static void main(String[] args) {
        Random random = new Random();
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        AppenderWithoutSync appender = new AppenderWithoutSync(list, random);
        ReducerWithoutSync reducer = new ReducerWithoutSync(list, random);

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
