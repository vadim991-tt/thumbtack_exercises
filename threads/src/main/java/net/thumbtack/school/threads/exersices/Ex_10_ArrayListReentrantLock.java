package net.thumbtack.school.threads.exersices;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReentrantArrayOperation {
    private final List<Integer> list;
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Random random = new Random();

    public ReentrantArrayOperation(List<Integer> list) {
        this.list = list;
    }

    public void remove() throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            lock.lock();
            try {
                while (list.isEmpty()) {
                    notEmpty.await();
                }
                int randomInt = random.nextInt(list.size());
                list.remove(randomInt);
                System.out.printf("Value was removed on %s index. Total remove actions: %s %n", randomInt, i);
            } finally {
                lock.unlock();
            }
        }
        System.out.println("Exiting reduce thread");
    }

    public void append() {
        for (int i = 0; i < 10000; i++) {
            lock.lock();
            try {
                int randomInt = random.nextInt(10000);
                list.add(randomInt);
                System.out.printf("%s was added to list. Total append actions: %s %n", randomInt, i);
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }
        System.out.println("Exiting append thread");
    }

}

class ReducerWithLock extends Thread {
    private final ReentrantArrayOperation operation;

    public ReducerWithLock(ReentrantArrayOperation operation) {
        this.operation = operation;
    }

    @Override
    public void run() {
        try {
            operation.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class AppenderWithLock extends Thread {
    private final ReentrantArrayOperation operation;

    public AppenderWithLock(ReentrantArrayOperation operation) {
        this.operation = operation;
    }

    @Override
    public void run() {
        operation.append();
    }
}


public class Ex_10_ArrayListReentrantLock {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        ReentrantArrayOperation operation = new ReentrantArrayOperation(list);
        AppenderWithLock appender = new AppenderWithLock(operation);
        ReducerWithLock reducer = new ReducerWithLock(operation);

        appender.start();
        reducer.start();

        try {
            appender.join();
            reducer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Current list size: %s %n", list.size());
        System.out.println("Exiting main thread");

    }


}
