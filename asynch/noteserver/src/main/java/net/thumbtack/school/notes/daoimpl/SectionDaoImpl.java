package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.base.ServerErrorCode;
import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Role;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.dao.SectionDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("sectionDaoImpl")
public class SectionDaoImpl extends DaoImplBase implements SectionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorDaoImpl.class);

    @Override
    public Section insertSection(Section section, Author author) throws ServerException {
        LOGGER.debug("DAO insert section with {} name. Requester id: {}", section.getName(), author.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getSectionMapper(sqlSession).insert(section, author.getId());
            } catch (RuntimeException e) {
                if(e.getMessage().contains("Duplicate entry")){
                    throw new ServerException(ServerErrorCode.INVALID_SECTION_NAME, section.getName(), "name");
                }
                LOGGER.info("Can't insert section {} {}", section.getName(), e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return section;
    }

    @Override
    public Section renameSection(Author author, String name, Integer id) throws ServerException {
        LOGGER.debug("DAO rename section. New name: {} Requester id: {}", name, author.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getSectionMapper(sqlSession).rename(id, author.getId(), name);
            } catch (RuntimeException e) {
                LOGGER.info("Can't rename section {} {}", name, e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return new Section(id, name);
    }

    @Override
    public Section getSectionInfo(Author author, int sectionId) throws ServerException {
        LOGGER.debug("DAO get information about section. Session id: {}", sectionId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getSectionMapper(sqlSession).getInformation(sectionId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get information about section: ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void deleteSection(Author author, int sectionId) throws ServerException {
        LOGGER.debug("DAO get information about section. Session id: {}", sectionId);
        try (SqlSession sqlSession = getSession()) {
            try {
                if (author.getRole() == Role.SUPER) {
                    getSectionMapper(sqlSession).deleteBySuper(sectionId);
                } else {
                    getSectionMapper(sqlSession).delete(sectionId, author.getId());
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't get information about section: ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }


    @Override
    public List<Section> getSections(Author author) throws ServerException {
        LOGGER.debug("DAO get all sections. Requester id {}", author.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                return getSectionMapper(sqlSession).getAll();
            } catch (RuntimeException e) {
                LOGGER.info("Can't get information about section: ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("DAO deleting sections");
        try (SqlSession sqlSession = getSession()) {
            try {
                getSectionMapper(sqlSession).deleteAll();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            sqlSession.commit();
        }
    }
}
