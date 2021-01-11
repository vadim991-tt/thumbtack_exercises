package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.dao.SectionDao;
import net.thumbtack.school.notes.dto.response.server.ServerSettingsDtoResponse;
import net.thumbtack.school.notes.utils.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("debugService")
public class DebugService {
    private final AuthorDao authorDao;
    private final SectionDao sectionDao;
    private final ConfigProperties properties;

    @Autowired
    public DebugService(AuthorDao authorDao, SectionDao sectionDao, ConfigProperties properties) {
        this.authorDao = authorDao;
        this.sectionDao = sectionDao;
        this.properties = properties;
    }

    public void clear() {
        sectionDao.deleteAll();
        authorDao.deleteAll();
    }

    public ServerSettingsDtoResponse getServerSettings() {
        return new ServerSettingsDtoResponse(properties.getMaxNameLength(), properties.getMinPasswordLength(), properties.getUser_idle_timeout());
    }
}
