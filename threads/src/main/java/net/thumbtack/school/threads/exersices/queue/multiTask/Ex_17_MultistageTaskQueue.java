package net.thumbtack.school.threads.exersices.queue.multiTask;

import net.thumbtack.school.threads.exersices.queue.model.Executable;
import net.thumbtack.school.threads.exersices.queue.model.MultistageTask;
import net.thumbtack.school.threads.exersices.queue.model.Task;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Consumer extends Thread {
    private final BlockingQueue<MultistageTask> queue;
    private final Random random = new Random();
    private final ConsumerData consumerData;
    private final Lock poisonLock;
    private final Condition areThreadsDoneTheirWork;

    public Consumer(BlockingQueue<MultistageTask> queue, ConsumerData consumerData, Lock poisonLock, Condition areThreadsDoneTheirWork) {
        this.queue = queue;
        this.consumerData = consumerData;
        this.poisonLock = poisonLock;
        this.areThreadsDoneTheirWork = areThreadsDoneTheirWork;
    }

    @Override
    public void run() {
        System.out.println("Consumer started");
        while (true) {
            try {
                // Checking if threads done their work
                try {
                    poisonLock.lock();
                    areThreadsDoneTheirWork.signal();
                } finally {
                    poisonLock.unlock();
                }
                MultistageTask multiTask = queue.take();
                List<Executable> list = multiTask.getList();
                if (list == null) {
                    break;
                }
                if (!list.isEmpty()) {
                    Executable executable = list.remove(random.nextInt(list.size()));
                    executable.execute();
                    consumerData.incrementTaskDone();
                }
                if (!list.isEmpty()) {
                    queue.put(multiTask);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Consumer finished");

    }

}


class Producer extends Thread {
    private final Queue<MultistageTask> queue;
    private final int multitaskQuantity;
    private final ProducerData producerData;

    public Producer(BlockingQueue<MultistageTask> queue, int multitaskQuantity, ProducerData producerData) {
        this.queue = queue;
        this.multitaskQuantity = multitaskQuantity;
        this.producerData = producerData;
    }

    @Override
    public void run() {
        producerData.incrementThreadsCreated();
        System.out.println("Producer Started");
        for (int i = 0; i < multitaskQuantity; i++) {
            LinkedList<Executable> list = new LinkedList<>();
            for (int tasks = 1; tasks < 11; tasks++) {
                producerData.incrementTasksCreated();
                list.add(new Task(String.format("MyTask%s by %s", tasks, Thread.currentThread().getName())));
            }
            queue.add(new MultistageTask("MultiTask", list));
        }
        producerData.incrementThreadsFinished();
        System.out.println("Producer finished");
    }
}

class ProducerData {
    private int producerThreadsCreated = 0;
    private int producerThreadsFinished = 0;
    private int tasksCreated = 0;

    public synchronized void incrementThreadsCreated() {
        producerThreadsCreated++;
    }

    public synchronized void incrementTasksCreated() {
        tasksCreated++;
    }

    public synchronized void incrementThreadsFinished() {
        producerThreadsFinished++;
    }

    public int getProducerThreadsCreated() {
        return producerThreadsCreated;
    }

    public int getProducerThreadsFinished() {
        return producerThreadsFinished;
    }

    public int getTasksCreated() {
        return tasksCreated;
    }
}

class ConsumerData {
    private int tasksDone = 0;

    public synchronized void incrementTaskDone() {
        tasksDone++;
    }

    public int getTasksDone() {
        return tasksDone;
    }
}


public class Ex_17_MultistageTaskQueue {
    public static void main(String[] args) throws Exception {
        int consumerThreads = 4;
        int producerThreads = 10;
        int multitasksByOneThread = 1;

        Lock poisonLock = new ReentrantLock();
        Condition areThreadsDoneWork = poisonLock.newCondition();
        ConsumerData consumerData = new ConsumerData();
        ProducerData producerData = new ProducerData();

        BlockingQueue<MultistageTask> queue = new LinkedBlockingDeque<>();
        Thread[] consumers = new Thread[consumerThreads];
        for (int i = 0; i < consumerThreads; i++) {
            consumers[i] = new Consumer(queue, consumerData, poisonLock, areThreadsDoneWork);
        }

        Thread[] producers = new Thread[producerThreads];
        for (int i = 0; i < producerThreads; i++) {
            producers[i] = new Producer(queue, multitasksByOneThread, producerData);
        }

        startThreads(producers);
        startThreads(consumers);
        joinThreads(producers);

        // Checking if threads done their work
        while (producerData.getTasksCreated() != consumerData.getTasksDone() || producerData.getProducerThreadsCreated() != producerData.getProducerThreadsFinished()) {
            poisonLock.lock();
            areThreadsDoneWork.await();
            poisonLock.unlock();
        }

        // Waiting for threads to finish their work
        for (int i = 0; i < consumerThreads; i++) {
            queue.add(new MultistageTask("Poison", null)); // Poisoning consumers
        }
        joinThreads(consumers);
        System.out.print("Is queue empty? " + queue.isEmpty()); // true
    }

    public static void startThreads(Thread[] threads) {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public static void joinThreads(Thread[] threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
