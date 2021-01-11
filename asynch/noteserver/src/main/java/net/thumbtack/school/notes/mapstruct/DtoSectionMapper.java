package net.thumbtack.school.notes.mapstruct;

import net.thumbtack.school.notes.dto.request.section.CreateSectionDtoRequest;
import net.thumbtack.school.notes.dto.request.section.RenameSectionDtoRequest;
import net.thumbtack.school.notes.dto.response.section.CreateSectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.SectionDtoResponse;
import net.thumbtack.school.notes.dto.response.section.RenameSectionDtoResponse;
import net.thumbtack.school.notes.model.Section;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DtoSectionMapper {

    DtoSectionMapper INSTANCE = Mappers.getMapper(DtoSectionMapper.class);

    Section sectionFromDto(CreateSectionDtoRequest request);

    CreateSectionDtoResponse dtoRespFromSection(Section section);

    Section sectionFromRenameDto(RenameSectionDtoRequest request);

    List<SectionDtoResponse> listSectionInfo(List<Section> list);

    RenameSectionDtoResponse dtoRenameRespFromSection(Section section);

    SectionDtoResponse infoDtoFromSection(Section section);


}
