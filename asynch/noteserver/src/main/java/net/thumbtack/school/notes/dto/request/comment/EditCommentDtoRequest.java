package net.thumbtack.school.notes.dto.request.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class EditCommentDtoRequest {
    private String body;
}
