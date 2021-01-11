package net.thumbtack.school.threads.exersices;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReaderWriter {
    static public int i = 0;
    static public ReadWriteLock lock = new ReentrantReadWriteLock();
}

class Writer extends Thread {
    @Override
    public void run() {
        ReaderWriter.lock.writeLock().lock();
        System.out.println("Write lock locked by thread " + Thread.currentThread().getId());
        try {
            ReaderWriter.i++;
            System.out.println("Writer thread " + Thread.currentThread().getId() + " set value " + ReaderWriter.i);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Write lock unlocked by thread " + Thread.currentThread().getId());
            ReaderWriter.lock.writeLock().unlock();
        }
    }
}

class Reader extends Thread {

    @Override
    public void run() {
        ReaderWriter.lock.readLock().lock();
        System.out.println("Read lock locked by thread " + Thread.currentThread().getId());
        try {
            System.out.println("Reader thread " + Thread.currentThread().getId() + " read value " + ReaderWriter.i);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Read lock unlocked by thread " + Thread.currentThread().getId());
            ReaderWriter.lock.readLock().unlock();
        }
    }
}



public class Ex_8_ReaderWriterDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Reader().start();
            new Writer().start();
            new Reader().start();
            new Writer().start();
        }
    }
}