package net.thumbtack.school.threads.exersices.queue.simpleTask;


import net.thumbtack.school.threads.exersices.queue.model.Task;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


class Consumer extends Thread {
    private final BlockingQueue<? super Task> queue;

    public Consumer(BlockingQueue<? super Task> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("Consumer Started");
        while (true) {
            try {
                Task task = (Task) queue.take();
                if (task.getName() == null) {
                    break;
                }
                task.execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Consumer Finished");
    }

}


class Producer extends Thread {
    private final Queue<? super Task> queue;
    private final int count;

    public Producer(BlockingQueue<? super Task> queue, int count) {
        this.queue = queue;
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println("Producer Started");
        for (int i = 0; i < count; i++) {
            queue.add(new Task("MyTask"));
        }
        System.out.println("Producer finished");
    }
}


public class Ex_16_TaskQueue {

    public static void main(String[] args)  {
        int consumerThreads = 5;
        int producerThreads = 10;
        int tasksProducingByOneThread = 2;

        BlockingQueue<? super Task> queue = new LinkedBlockingDeque<>();
        Thread[] consumers = new Thread[consumerThreads];
        for (int i = 0; i < consumerThreads; i++) {
            consumers[i] = new Consumer(queue);
        }

        Thread[] producers = new Thread[producerThreads];
        for (int i = 0; i < producerThreads; i++) {
            producers[i] = new Producer(queue, tasksProducingByOneThread);
        }

        startThreads(producers);
        startThreads(consumers);
        joinThreads(producers);
//         Poisoning consumer threads after producers done their work
        for (int i = 0; i < consumerThreads; i++) {
            queue.add(new Task(null));
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
