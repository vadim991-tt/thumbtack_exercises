
package net.thumbtack.school.threads.exersices;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class PingPongReentrantLock extends Thread {
    private final Lock lock = new ReentrantLock();
    private final Condition pinged;
    private final Condition ponged;
    private boolean firstPing = true;

    public PingPongReentrantLock() {
        pinged = lock.newCondition();
        ponged = lock.newCondition();
    }

    public void ping() {
        try {
            lock.lock();
            if (firstPing) {
                firstPing = false;
            }
            System.out.println("Ping");
            Thread.sleep(200);
            pinged.signalAll();
            ponged.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void pong() {
        try {
            lock.lock();
            while (firstPing) {
                ponged.await();
            }
            System.out.println("Pong");
            Thread.sleep(200);
            ponged.signalAll();
            pinged.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}

public class Ex_11_PingPongLockDemo {

    public static void main(String... arg) {
        PingPongReentrantLock pingPong = new PingPongReentrantLock();

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

