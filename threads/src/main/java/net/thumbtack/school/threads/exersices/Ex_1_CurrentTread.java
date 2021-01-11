package net.thumbtack.school.threads.exersices;


public class Ex_1_CurrentTread {
    public static void getThreadInfo() {
        Thread t = Thread.currentThread();
        System.out.println(t.getId());
        System.out.println(t.getName());
        System.out.println(t.getPriority());
        System.out.println(t.getState());
        System.out.println(t.isAlive());
    }

    public static void main(String[] args) {
        getThreadInfo();
    }
}