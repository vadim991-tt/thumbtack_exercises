package net.thumbtack.school.spring.music;

import net.thumbtack.school.spring.model.Recording;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

public interface PublishingChannel {

    Logger LOGGER = LoggerFactory.getLogger(PublishingChannel.class);

    void publish(Recording recording, ZonedDateTime publishAvailableDate);

    void delete(int id);

}
