package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Section;

import java.util.List;

public interface SectionDao {

    Section insertSection(Section section, Author author) throws ServerException;

    Section renameSection( Author author, String name, Integer id) throws ServerException;

    Section getSectionInfo(Author author, int sectionId) throws ServerException;

    void deleteSection(Author author, int sectionId) throws ServerException;

    List<Section> getSections(Author author) throws ServerException;

    void deleteAll();
}
