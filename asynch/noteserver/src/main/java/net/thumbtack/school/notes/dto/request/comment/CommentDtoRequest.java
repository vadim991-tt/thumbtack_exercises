package net.thumbtack.school.notes.dto.request.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class CommentDtoRequest {
    private String body;
    private int noteId;
}
