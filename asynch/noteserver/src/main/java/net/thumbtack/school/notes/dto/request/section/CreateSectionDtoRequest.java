package net.thumbtack.school.notes.dto.request.section;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validator.section.ValidSection;

@AllArgsConstructor
@NoArgsConstructor
@ValidSection
public @Data
class CreateSectionDtoRequest {
    private String name;
}
