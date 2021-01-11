package net.thumbtack.school.threads.exersices.queue.data;

import net.thumbtack.school.threads.exersices.queue.model.Data;

import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;


class Consumer extends Thread {
    private final BlockingQueue<Data> queue;

    public Consumer(BlockingQueue<Data> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("Consumer Started");
        while (true) {
            try {
                Data date = queue.take();
                int[] dataArray = date.get();
                if (dataArray == null) {
                    break;
                }
                System.out.println(Arrays.toString(dataArray));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Consumer finished");
    }

}


class Producer extends Thread {
    private static final Random random = new Random();
    private final Queue<Data> queue;
    private final int count;

    public Producer(BlockingQueue<Data> queue, int count) {
        this.queue = queue;
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println("Producer Started");
        for (int i = 0; i < count; i++) {
            queue.add(new Data(random.ints(100).toArray()));
        }
        System.out.println("Producer finished");
    }
}


public class Ex_15_DataQueue {


    public static void main(String[] args) {
        int consumerThreads = 10;
        int producerThreads = 20;
        int dataProducingByOneThread = 2;

        BlockingQueue<Data> queue = new LinkedBlockingQueue<>();
        Thread[] consumers = new Thread[consumerThreads];
        for (int i = 0; i < consumerThreads; i++) {
            consumers[i] = new Consumer(queue);
        }

        Thread[] producers = new Thread[producerThreads];
        for (int i = 0; i < producerThreads; i++) {
            producers[i] = new Producer(queue, dataProducingByOneThread);
        }

        startThreads(producers);
        startThreads(consumers);
        joinThreads(producers);
        // Poisoning consumer threads after producers done their work
        for (int i = 0; i < consumerThreads; i++) {
            queue.add(new Data(null));
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
