package net.thumbtack.school.notes.dto.request.note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class CreateNoteDtoRequest {
    @NotBlank
    private String subject;
    @NotBlank
    private String body;
    private Integer sectionId;
}
