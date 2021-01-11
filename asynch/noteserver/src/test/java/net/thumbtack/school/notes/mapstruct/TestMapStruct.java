package net.thumbtack.school.notes.mapstruct;

import net.thumbtack.school.notes.dto.request.author.RegisterAuthorDtoRequest;
import net.thumbtack.school.notes.dto.response.author.RegisterAuthorDtoResponse;
import net.thumbtack.school.notes.dto.response.note.CreateNoteDtoResponse;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Revision;
import net.thumbtack.school.notes.model.Section;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class TestMapStruct {

    @Test
    public void AuthorToDtoTest() {
        RegisterAuthorDtoRequest req = new RegisterAuthorDtoRequest("firstName", "lastName", "patr", "login", "pass");
        Author authorFromDto = DtoAuthorMapper.INSTANCE.authorFromRegisterDto(req);

        Author author = new Author("FirstName", "lastName", "patronymc", "login", "pass");
        RegisterAuthorDtoResponse dtoResponse = DtoAuthorMapper.INSTANCE.responseFromAuthor(author);

        assertEquals(req.getFirstName(), authorFromDto.getFirstName());
        assertEquals(author.getFirstName(), dtoResponse.getFirstName());
    }

    @Test
    public void dtoFromNote() {
        Author author = new Author("FirstName", "lastName", "patronymc", "login", "pass");
        author.setId(1);
        Section section = new Section(2, "dadada");
        Revision revision = new Revision(3, "body", null, null);
        List<Revision> revisions = new ArrayList<>();
        revisions.add(revision);
        Note note = new Note(4, "subject", "body", 0, section, author, new Timestamp(1000000000000L), revisions);

        CreateNoteDtoResponse createNoteDtoResponse = DtoNoteMapper.INSTANCE.createNoteRespFromNote(note);
        assertEquals(note.getBody(), createNoteDtoResponse.getBody());
    }
}
