package net.thumbtack.school.threads.exersices;

class MyThread extends Thread {
    private final String name;

    public MyThread(String threadName) {
        name = threadName;
    }

    public void run() {
        try {
            System.out.println(name + "has been created");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(name + "Interrupted");
        }
        System.out.println(name + " exiting.");
    }
}

public class Ex_3_CreateThreeThreads {

    public static void main(String[] args) {
        MyThread firstThread = new MyThread("First thread");
        MyThread secondThread = new MyThread("Second thread");
        MyThread thirdThread = new MyThread("Third tread");

        firstThread.start();
        secondThread.start();
        thirdThread.start();

        System.out.println("Thread One is alive: " + firstThread.isAlive());
        System.out.println("Thread Two is alive: " + secondThread.isAlive());
        System.out.println("Thread Three is alive: " + thirdThread.isAlive());

        try {
            firstThread.join();
            secondThread.join();
            thirdThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
    }
}
