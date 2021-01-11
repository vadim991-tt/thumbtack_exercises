package net.thumbtack.school.notes.mapstruct;

import net.thumbtack.school.notes.dto.request.note.CreateNoteDtoRequest;
import net.thumbtack.school.notes.dto.response.notes.CommentDto;
import net.thumbtack.school.notes.dto.response.notes.NoteDto;
import net.thumbtack.school.notes.dto.response.notes.RevisionDto;
import net.thumbtack.school.notes.dto.response.note.ChangeNoteDtoResponse;
import net.thumbtack.school.notes.dto.response.note.CreateNoteDtoResponse;
import net.thumbtack.school.notes.dto.response.note.NoteInfoDtoResponse;
import net.thumbtack.school.notes.model.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface DtoNoteMapper {

    DtoNoteMapper INSTANCE = Mappers.getMapper(DtoNoteMapper.class);

    Note noteFromCreateNoteDto(CreateNoteDtoRequest request);

    @Mapping(source = "author", target = "authorId", qualifiedByName = "idFromAuthor")
    @Mapping(source = "revisions", target = "revisionId", qualifiedByName = "idFromRevision")
    @Mapping(source = "section", target = "sectionId", qualifiedByName = "idFromSection")
    CreateNoteDtoResponse createNoteRespFromNote(Note note);

    @Mapping(source = "author", target = "authorId", qualifiedByName = "idFromAuthor")
    @Mapping(source = "revisions", target = "revisionId", qualifiedByName = "idFromRevision")
    @Mapping(source = "section", target = "sectionId", qualifiedByName = "idFromSection")
    NoteInfoDtoResponse dtoInfoFromNote(Note note);

    @Mapping(source = "author", target = "authorId", qualifiedByName = "idFromAuthor")
    @Mapping(source = "revisions", target = "revisionId", qualifiedByName = "idFromRevision")
    @Mapping(source = "section", target = "sectionId", qualifiedByName = "idFromSection")
    ChangeNoteDtoResponse changeNoteDtoResp(Note note);

    @Named("idFromAuthor")
    static int idFromAuthor(Author author) {
        return author.getId();
    }

    @Named("idFromRevision")
    static int idFromRevision(List<Revision> revisions) {
        return revisions.remove(0).getId();
    }

    @Named("idFromSection")
    static int idFromSection(Section section) {
        return section.getId();
    }

    @IterableMapping(qualifiedByName = "dtoNote")
    List<NoteDto> dtoFromNotes(List<Note> notes);

    @Named("dtoNote")
    @Mapping(source = "author", target = "authorId", qualifiedByName = "idFromAuthor")
    @Mapping(source = "section", target = "sectionId", qualifiedByName = "idFromSection")
    @Mapping(source = "revisions", target = "revisions", qualifiedByName = "dtoFromRevisions")
    NoteDto dtoNote(Note note);

    @Named("dtoFromRevisions")
    @IterableMapping(qualifiedByName = "dtoRevision")
    List<RevisionDto> dtoFromRevisions(List<Revision> revisions);

    @Named("dtoRevision")
    @Mapping(source = "comments", target = "comments", qualifiedByName = "dtoFromComments")
    RevisionDto dtoRevision(Revision revision);

    @Named("dtoFromComments")
    @IterableMapping(qualifiedByName = "dtoComment")
    List<CommentDto> dtoFromComments(List<Comment> comments);

    @Named("dtoComment")
    @Mapping(source = "author", target = "authorId", qualifiedByName = "idFromAuthor")
    CommentDto dtoComment(Comment comment); // TODO: ended here

}

