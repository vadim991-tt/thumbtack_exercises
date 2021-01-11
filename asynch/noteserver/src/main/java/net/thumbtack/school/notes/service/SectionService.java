package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.dao.SectionDao;
import net.thumbtack.school.notes.dto.request.section.CreateSectionDtoRequest;
import net.thumbtack.school.notes.dto.request.section.RenameSectionDtoRequest;
import net.thumbtack.school.notes.dto.response.section.CreateSectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.SectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.RenameSectionDtoResponse;
import net.thumbtack.school.notes.mapstruct.DtoSectionMapper;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sectionService")
public class SectionService {

    private final SectionDao sectionDao;
    private final AuthorDao authorDao;

    @Autowired
    public SectionService(SectionDao sectionDao, AuthorDao authorDao) {
        this.sectionDao = sectionDao;
        this.authorDao = authorDao;
    }

    public CreateSectionDtoResponse createSection(CreateSectionDtoRequest req, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author requester = authorDao.getAuthorByUUID(uuid);
        Section section = DtoSectionMapper.INSTANCE.sectionFromDto(req);
        section = sectionDao.insertSection(section, requester);
        return DtoSectionMapper.INSTANCE.dtoRespFromSection(section);
    }

    public RenameSectionDtoResponse renameSection(RenameSectionDtoRequest req, String uuid, int id) throws ServerException {
        authorDao.validateToken(uuid);
        Author requester = authorDao.getAuthorByUUID(uuid);
        Section section = sectionDao.renameSection(requester, req.getName(), id);
        return DtoSectionMapper.INSTANCE.dtoRenameRespFromSection(section);
    }

    public void deleteSection(String uuid, int id) throws ServerException {
        authorDao.validateToken(uuid);
        Author requester = authorDao.getAuthorByUUID(uuid);
        sectionDao.deleteSection(requester, id);
    }

    public SectionDtoResponse getSectionInfo(String uuid, int id) throws ServerException {
        authorDao.validateToken(uuid);
        Author requester = authorDao.getAuthorByUUID(uuid);
        Section section = sectionDao.getSectionInfo(requester, id);
        return DtoSectionMapper.INSTANCE.infoDtoFromSection(section);
    }

    public List<SectionDtoResponse> getSections(String uuid) throws ServerException {
        Author requester = authorDao.getAuthorByUUID(uuid);
        List<Section> sections = sectionDao.getSections(requester);
        return DtoSectionMapper.INSTANCE.listSectionInfo(sections);
    }


}
