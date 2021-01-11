package net.thumbtack.school.notes.dto.response.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class RevisionDto {
    private int id;
    private String body;
    private String created;
    private List<CommentDto> comments;
}
