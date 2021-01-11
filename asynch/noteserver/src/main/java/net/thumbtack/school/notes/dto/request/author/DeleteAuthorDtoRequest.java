package net.thumbtack.school.notes.dto.request.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validator.author.ValidPassword;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class DeleteAuthorDtoRequest {
    @ValidPassword
    private String password;
}
