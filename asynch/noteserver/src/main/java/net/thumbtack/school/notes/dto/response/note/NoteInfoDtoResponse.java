package net.thumbtack.school.notes.dto.response.note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public  @Data
class NoteInfoDtoResponse {
    private int id;
    private String subject;
    private String body;
    private int sectionId;
    private int authorId;
    private String created;
    private int revisionId;
}
