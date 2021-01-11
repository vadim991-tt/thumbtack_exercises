package net.thumbtack.school.threads.exersices;

import java.util.concurrent.Semaphore;

class SemaphorePingPong {

    // Start Pong consumer semaphore unavailable.
    static Semaphore semPing = new Semaphore(1);
    static Semaphore semPong = new Semaphore(0);

    public void ping() {
        try {
            semPing.acquire();
            System.out.println("Ping");
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        } finally {
            semPong.release();
        }
    }

    public void pong() {
        try {
            semPong.acquire();
            System.out.println("Pong");
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        } finally {
            semPing.release();

        }
    }
}

public class Ex_7_PingPong {
    public static void main(String[] args) {
        SemaphorePingPong pingPong = new SemaphorePingPong();

        new Thread(() -> {
            while (true)
                pingPong.ping();
        }).start();

        new Thread(() -> {
            while (true)
                pingPong.pong();
        }).start();

    }
}