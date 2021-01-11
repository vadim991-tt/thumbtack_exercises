package net.thumbtack.school.notes.dto.request.note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class RateNoteDtoRequest {
    @Max(5)
    private int rating;
}
