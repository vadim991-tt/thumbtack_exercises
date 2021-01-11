package net.thumbtack.school.notes.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class CommentDtoResponse {
    private int id;
    private String body;
    private int noteId;
    private int authorId;
    private int revisionId;
    private Timestamp created;
}
