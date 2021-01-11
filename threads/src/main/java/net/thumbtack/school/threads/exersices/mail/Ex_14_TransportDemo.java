package net.thumbtack.school.threads.exersices.mail;


import net.thumbtack.school.threads.model.Message;

import java.io.*;
import java.util.*;


class Transport extends Thread {

    private final BufferedWriter bufferedWriter;
    private final Message message;

    public Transport(BufferedWriter bufferedWriter, Message message) {
        this.bufferedWriter = bufferedWriter;
        this.message = message;
    }

    @Override
    public void run() {
        send(message);
    }

    public void send(Message message) {
        try {
            Thread.sleep(200); // Sending email simulation;
            bufferedWriter.write(message.toString() + " was send by: " + Thread.currentThread().getName() + "\n");
            bufferedWriter.flush();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Ex_14_TransportDemo {

    public static void main(String[] args) throws IOException {
        int mailsNumber = 10000;

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Mails.txt"));
        LinkedList<String> list = new LinkedList<>();

        fillFileWithEmails(mailsNumber);
        fillListWithEmails(list);

        Thread[] threads = new Thread[mailsNumber];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Transport(bufferedWriter, new Message(list.remove(), "MyProgram", "Subject", "Body"));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bufferedWriter.close();
    }

    public static void fillFileWithEmails(int size) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Addresses.txt"))) {
            for (int i = 0; i < size; i++) {
                bw.write(String.format("emailNumber%s@mail.ru%n", i));
            }
        }
    }

    public static void fillListWithEmails(List<String> emails) {
        try (BufferedReader br = new BufferedReader(new FileReader("Addresses.txt"))) {
            String line = br.readLine();
            while (line != null) {
                emails.add(line);
                line = br.readLine();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
