package net.thumbtack.school.notes;


import net.thumbtack.school.notes.utils.MyBatisUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotesApplication {

    public static void main(String[] args) {
        initSqlSessionFactory();
        SpringApplication.run(NotesApplication.class, args);
    }

    public static void initSqlSessionFactory(){
        boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();
        if (!initSqlSessionFactory) {
            throw new RuntimeException("Can't create connection, stop");
        }
    }

}
