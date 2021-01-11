package net.thumbtack.school.notes.endpoint;


import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dto.request.note.ChangeNoteDtoRequest;
import net.thumbtack.school.notes.dto.request.note.CreateNoteDtoRequest;
import net.thumbtack.school.notes.dto.request.note.RateNoteDtoRequest;
import net.thumbtack.school.notes.dto.response.comment.CommentDtoResponse;
import net.thumbtack.school.notes.dto.response.note.ChangeNoteDtoResponse;
import net.thumbtack.school.notes.dto.response.note.CreateNoteDtoResponse;
import net.thumbtack.school.notes.dto.response.note.NoteInfoDtoResponse;
import net.thumbtack.school.notes.dto.response.notes.NoteDto;
import net.thumbtack.school.notes.service.CommentService;
import net.thumbtack.school.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NotesEndpoint {
    private final NoteService noteService;
    private final CommentService commentService;

    @Autowired
    public NotesEndpoint(NoteService noteService, CommentService commentService) {
        this.noteService = noteService;
        this.commentService = commentService;
    }

    @PostMapping
    public CreateNoteDtoResponse insertNote(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                            @RequestBody @Valid CreateNoteDtoRequest req) throws ServerException {
        return noteService.createNote(req, javaSessionId);
    }

    @GetMapping(value = "/{id}")
    public NoteInfoDtoResponse getNoteInfo(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                           @PathVariable("id") int id) throws ServerException {

        return noteService.getNoteInfo(id, javaSessionId);
    }


    @PutMapping("/{id}")
    public ChangeNoteDtoResponse changeNote(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                            @RequestBody @Valid ChangeNoteDtoRequest req,
                                            @PathVariable("id") int id) throws ServerException {
        return noteService.changeNoteInfo(id, req, javaSessionId);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String javaSessionId) throws ServerException {
        noteService.deleteNote(id, javaSessionId);
    }

    @GetMapping(value = "/{id}/comments")
    public List<CommentDtoResponse> getComments(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                                @PathVariable("id") int noteId) throws ServerException {
        return commentService.getComments(noteId, javaSessionId);

    }

    @DeleteMapping(value = "/{id}/comments")
    public void deleteCommentsFromNote(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                       @PathVariable("id") int noteId) throws ServerException {
        commentService.deleteCommentsFromNote(noteId, javaSessionId);
    }

    @PostMapping(value = "/{id}/rating")
    public void rateNote(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                         @RequestBody @Valid RateNoteDtoRequest req,
                         @PathVariable("id") int noteId) throws ServerException {
        noteService.rateNote(noteId, req, javaSessionId);
    }

    @GetMapping
    public List<NoteDto> getNotes(@CookieValue(value = "JAVASESSIONID") String javaSessionId,
                                  @RequestParam Map<String, String> allParams) throws ServerException {
        List<NoteDto> dtos = noteService.getNotes(javaSessionId, allParams);
        return dtos;
    }

}
