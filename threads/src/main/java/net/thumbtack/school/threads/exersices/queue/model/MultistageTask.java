package net.thumbtack.school.threads.exersices.queue.model;

import java.util.LinkedList;
import java.util.List;

public class MultistageTask implements Executable {
    private String name;
    private List<Executable> list;

    public MultistageTask(String name, List<Executable> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Executable> getList() {
        return list;
    }

    public void setList(LinkedList<Executable> list) {
        this.list = list;
    }

    @Override
    public void execute() {
        System.out.printf("%s doing %s%n", Thread.currentThread().getName(), getName());
    }
}