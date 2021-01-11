package net.thumbtack.school.spring.data;

import net.thumbtack.school.spring.model.Recording;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DataStorage {

    Logger LOGGER = LoggerFactory.getLogger(DataStorage.class);

    String save(String path);

    Recording save(Recording recording);

    Recording get(int id);
}
