package net.thumbtack.school.notes.dto.request.note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validator.note.ValidChangeNoteRequest;

@AllArgsConstructor
@NoArgsConstructor
@ValidChangeNoteRequest
public @Data
class ChangeNoteDtoRequest {
    private String body;
    private Integer sectionId;
}
