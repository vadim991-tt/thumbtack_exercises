package net.thumbtack.school.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        System.out.println("Start application");
        SpringApplication.run(Application.class);
        //System.out.println("Stop application");
    }
}
