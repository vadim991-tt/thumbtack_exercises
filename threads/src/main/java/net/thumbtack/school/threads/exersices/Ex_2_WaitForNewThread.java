package net.thumbtack.school.threads.exersices;

public class Ex_2_WaitForNewThread {

    public static void main(String[] args) {
        Runnable runnable = () -> {
            try {
                System.out.println("Child thread was created");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Child interrupted.");
            }
            System.out.println("Exiting child thread.");

        };

        Thread child = new Thread(runnable);
        child.start();

        System.out.println("Waiting for child thread");
        try {
            child.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }
        System.out.println("Exiting main thread.");
    }
}
