package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.dao.AuthorDao;
import net.thumbtack.school.notes.dao.NoteDao;
import net.thumbtack.school.notes.dto.request.note.ChangeNoteDtoRequest;
import net.thumbtack.school.notes.dto.request.note.CreateNoteDtoRequest;
import net.thumbtack.school.notes.dto.request.note.RateNoteDtoRequest;
import net.thumbtack.school.notes.dto.response.note.ChangeNoteDtoResponse;
import net.thumbtack.school.notes.dto.response.note.CreateNoteDtoResponse;
import net.thumbtack.school.notes.dto.response.note.NoteInfoDtoResponse;
import net.thumbtack.school.notes.dto.response.notes.CommentDto;
import net.thumbtack.school.notes.dto.response.notes.NoteDto;
import net.thumbtack.school.notes.dto.response.notes.RevisionDto;
import net.thumbtack.school.notes.mapstruct.DtoNoteMapper;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("noteService")
public class NoteService {

    private final NoteDao noteDao;
    private final AuthorDao authorDao;

    @Autowired
    public NoteService(NoteDao noteDao, AuthorDao authorDao) {
        this.noteDao = noteDao;
        this.authorDao = authorDao;
    }

    public CreateNoteDtoResponse createNote(CreateNoteDtoRequest req, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author requester = authorDao.getAuthorByUUID(uuid);
        int sectionId = req.getSectionId();
        Note note = DtoNoteMapper.INSTANCE.noteFromCreateNoteDto(req);
        note = noteDao.insertNote(requester, note, sectionId);
        return DtoNoteMapper.INSTANCE.createNoteRespFromNote(note);
    }

    public NoteInfoDtoResponse getNoteInfo(int id, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author requester = authorDao.getAuthorByUUID(uuid);
        Note note = noteDao.getNoteInfo(requester, id);
        return DtoNoteMapper.INSTANCE.dtoInfoFromNote(note);
    }

    public ChangeNoteDtoResponse changeNoteInfo(Integer id, ChangeNoteDtoRequest request, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author requester = authorDao.getAuthorByUUID(uuid);
        String body = request.getBody();
        Integer sectionId = request.getSectionId();
        Note note = null;
        if (body != null && sectionId != null) {
            note = noteDao.changeBodyAndMove(requester, id, sectionId, body);
        } else if (body == null && sectionId != null) {
            note = noteDao.move(requester, id, sectionId);
        } else if (body != null) {
            note = noteDao.changeBody(requester, id, body);
        }
        return DtoNoteMapper.INSTANCE.changeNoteDtoResp(note);
    }

    public void deleteNote(int id, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author requester = authorDao.getAuthorByUUID(uuid);
        noteDao.deleteNote(requester, id);
    }

    public void rateNote(int id, RateNoteDtoRequest req, String uuid) throws ServerException {
        authorDao.validateToken(uuid);
        Author requester = authorDao.getAuthorByUUID(uuid);
        noteDao.rateNote(requester, id, req.getRating());
    }

    public List<NoteDto> getNotes(String uuid, Map<String, String> params) throws ServerException {
        authorDao.validateToken(uuid);
        List<Note> notes = noteDao.getAll(params);
        return createDtoResponseByParams(notes, params);
    }

    // TODO :finish
    private static List<NoteDto> createDtoResponseByParams(List<Note> notes, Map<String, String> params) {
        String commentsCondition = params.get("comments");
        String allVersionsCondition = params.get("allVersions");

        boolean comments = commentsCondition != null && commentsCondition.equals("true");
        boolean allVersions = allVersionsCondition != null && allVersionsCondition.equals("true");

        List<NoteDto> noteList = DtoNoteMapper.INSTANCE.dtoFromNotes(notes);

        if (!allVersions && comments) {
            for (NoteDto note : noteList) {
                List<CommentDto> commentDtoList = new ArrayList<>();
                List<RevisionDto> revisionList = note.getRevisions();
                for (RevisionDto revision : revisionList) {
                    commentDtoList.addAll(revision.getComments());
                }
                note.setComments(commentDtoList);
                note.setRevisions(null);
            }
        } else if (allVersions && !comments) {
            for (NoteDto note : noteList) {
                for (RevisionDto revision : note.getRevisions()) {
                    revision.setComments(null);
                }
            }
        } else if (!allVersions && !comments) {
            for (NoteDto note : noteList) {
                note.setRevisions(null);
            }
        }

        return noteList;
    }

}
