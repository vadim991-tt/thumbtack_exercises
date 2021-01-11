package net.thumbtack.school.spring.music;

import net.thumbtack.school.spring.model.Recording;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component("YouTube")
public class YoutubeMusicChannel implements PublishingChannel {

    @Override
    public void publish(Recording recording, ZonedDateTime publishAvailableDate) {
        LOGGER.info("{} {} was added successfully [YouTube] with {} type", publishAvailableDate, recording.getName(), recording.getType());
    }


    @Override
    public void delete(int id) {
        LOGGER.info("Song with {} ID was deleted successfully [YouTube]", id);
    }
}
