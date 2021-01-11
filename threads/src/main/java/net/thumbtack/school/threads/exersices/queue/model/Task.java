package net.thumbtack.school.threads.exersices.queue.model;

public class Task implements Executable {
    private String name;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void execute() {
        System.out.printf("%s doing %s%n", Thread.currentThread().getName(), getName());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s done by %s%n", getName(), Thread.currentThread().getName());

    }
}