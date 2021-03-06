package net.thumbtack.school.spring.operations;

import net.thumbtack.school.spring.data.DataStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SaveVideo implements CommandLineRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(SaveVideo.class);

    private final DataStorage dataStorage;

    @Autowired
    public SaveVideo(@Qualifier("videoStorage") DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public void run(String... args) {
        LOGGER.info("Runner was started");
        LOGGER.info("Saving song from {}", "json_songs/some_video.txt");
        String uuid = dataStorage.save("json_songs/some_video.txt");
        LOGGER.info("Song was successfully saved. UUID: {}", uuid);
        LOGGER.info("Runner was stopped");
    }
}
