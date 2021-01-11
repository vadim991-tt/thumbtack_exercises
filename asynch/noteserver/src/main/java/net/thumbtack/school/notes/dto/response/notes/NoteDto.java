package net.thumbtack.school.notes.dto.response.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class NoteDto {
    private int id;
    private String subject;
    private String body;
    private int sectionId;
    private int authorId;
    private String created;
    private List<RevisionDto> revisions;
    private List<CommentDto> comments;
}
