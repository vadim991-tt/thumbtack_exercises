package net.thumbtack.school.spring.music;


import net.thumbtack.school.spring.model.Recording;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component("Yandex")
public class YandexMusicChannel implements PublishingChannel {


    @Override
    public void publish(Recording recording, ZonedDateTime publishAvailableDate) {
        LOGGER.info("{} {} was added successfully [Yandex]", publishAvailableDate, recording.getName());
    }


    @Override
    public void delete(int id) {
        LOGGER.info("Song with {} ID was deleted successfully [Yandex]", id);
    }


}
