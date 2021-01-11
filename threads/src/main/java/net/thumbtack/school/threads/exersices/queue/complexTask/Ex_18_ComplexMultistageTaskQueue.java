package net.thumbtack.school.threads.exersices.queue.complexTask;

import net.thumbtack.school.threads.exersices.queue.model.EventType;
import net.thumbtack.school.threads.exersices.queue.model.Executable;
import net.thumbtack.school.threads.exersices.queue.model.MultistageTask;
import net.thumbtack.school.threads.exersices.queue.model.Task;


import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


class Consumer extends Thread {
    private final BlockingQueue<MultistageTask> taskQueue;
    private final BlockingQueue<Event> eventQueue;
    private final Random random;

    public Consumer(BlockingQueue<MultistageTask> taskQueue, BlockingQueue<Event> eventQueue, Random random) {
        this.taskQueue = taskQueue;
        this.random = random;
        this.eventQueue = eventQueue;
    }

    @Override
    public void run() {
        System.out.println("Consumer started");
        while (true) {
            try {
                MultistageTask multiTask = taskQueue.take();
                List<Executable> list = multiTask.getList();
                if (list == null) {
                    break;
                }
                if (!list.isEmpty()) {
                    Executable executable = list.remove(random.nextInt(list.size()));
                    executable.execute();
                    eventQueue.put(new Event(EventType.TASK_DONE));
                }
                if (!list.isEmpty()) {
                    taskQueue.put(multiTask);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Consumer finished");
    }
}


class Producer extends Thread {

    private final BlockingQueue<MultistageTask> taskQueue;
    private final BlockingQueue<Event> eventQueue;
    private final int multiTasks;
    private final Random random;


    public Producer(BlockingQueue<MultistageTask> taskQueue, BlockingQueue<Event> eventQueue, int multiTasks, Random random) {
        this.multiTasks = multiTasks;
        this.eventQueue = eventQueue;
        this.taskQueue = taskQueue;
        this.random = random;
    }

    @Override
    public void run() {
        System.out.println("Producer Started");
        eventQueue.add(new Event(EventType.PRODUCER_CREATED));
        boolean createNewDeveloper = random.nextBoolean();
        if (createNewDeveloper) {
            createNewDeveloper();
        } else {
            addTask();
        }
        eventQueue.add(new Event(EventType.PRODUCER_FINISHED));
        System.out.println("Producer finished");
    }

    public void createNewDeveloper() {
        Producer producer = new Producer(taskQueue, eventQueue, multiTasks, random);
        producer.start();
        try {
            producer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addTask() {
        for (int i = 0; i < multiTasks; i++) {
            LinkedList<Executable> list = new LinkedList<>();
            for (int tasks = 1; tasks < 11; tasks++) {
                eventQueue.add(new Event(EventType.TASK_CREATED));
                list.add(new Task(String.format("MyTask%s by %s", tasks, Thread.currentThread().getName())));
            }
            taskQueue.add(new MultistageTask("MultiTask", list));
        }
    }
}

class Event {
    private final EventType type;

    public Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}

public class Ex_18_ComplexMultistageTaskQueue {
    public static void main(String[] args) throws InterruptedException {
        int consumerThreads = 5;
        int producerThreads = 5;
        int tasksProducingByOneThread = 2;

        Random random = new Random();
        BlockingQueue<MultistageTask> taskQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();

        int tasksCreated = 0;
        int tasksDone = 0;
        int producersCreated = 0;
        int producersFinished = 0;

        Thread[] consumers = new Thread[consumerThreads];
        for (int i = 0; i < consumerThreads; i++) {
            consumers[i] = new Consumer(taskQueue, eventQueue, random);
        }

        Thread[] producers = new Thread[producerThreads];
        for (int i = 0; i < producerThreads; i++) {
            producers[i] = new Producer(taskQueue, eventQueue, tasksProducingByOneThread, random);
        }

        startThreads(producers);
        startThreads(consumers);
        joinThreads(producers);

        // Waiting for threads to finish their work
        while (!(tasksCreated == tasksDone && producersCreated == producersFinished) || tasksCreated == 0) {
            Event event = eventQueue.take();
            switch (event.getType()) {
                case TASK_DONE:
                    tasksDone++;
                    break;
                case TASK_CREATED:
                    tasksCreated++;
                    break;
                case PRODUCER_CREATED:
                    producersCreated++;
                    break;
                case PRODUCER_FINISHED:
                    producersFinished++;
                    break;
            }
        }
        for (int i = 0; i < consumerThreads; i++) {
            taskQueue.add(new MultistageTask("Poison", null)); // Poisoning consumers
        }
        joinThreads(consumers);
        System.out.print("Is taskQueue empty? " + taskQueue.isEmpty()); // true
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
