package net.thumbtack.school.notes.dto.response.section;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class CreateSectionDtoResponse {
    private String name;
    private int id;
}
