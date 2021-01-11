package net.thumbtack.school.notes.dto.response.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class CommentDto {
    private int id;
    private String body;
    private int authorId;
    private int revisionId;
    private String created;
}
