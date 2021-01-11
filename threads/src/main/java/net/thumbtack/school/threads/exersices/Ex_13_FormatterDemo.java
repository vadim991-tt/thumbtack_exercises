package net.thumbtack.school.threads.exersices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

class Formatter {

    private final ThreadLocal<SimpleDateFormat> dateThreadLocal = new ThreadLocal<>();

    public void setDateThreadLocal(SimpleDateFormat formatter) {
        this.dateThreadLocal.set(formatter);
    }

    public String format(Date date) {
        return dateThreadLocal.get().format(date);
    }

}

class FormatterThread extends Thread {
    private final Formatter formatter;

    public FormatterThread(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void run() {
        formatter.setDateThreadLocal(new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"));
        Date date = new Date(1606201734985L + new Random().nextInt(900000000)); // Random date creation
        String dateBefore = (String.format("Current thread is %s. Date before: %s", currentThread().getName(), date));
        System.out.println("Before ----> : " + dateBefore);
        String formattedDate = formatter.format(date);
        System.out.println("After ----> : " + formattedDate);

    }
}

public class Ex_13_FormatterDemo {
    public static void main(String[] args) {
        Formatter formatter = new Formatter();
        Thread thread1 = new FormatterThread(formatter);
        Thread thread2 = new FormatterThread(formatter);
        Thread thread3 = new FormatterThread(formatter);
        Thread thread4 = new FormatterThread(formatter);
        Thread thread5 = new FormatterThread(formatter);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }

    }
}
