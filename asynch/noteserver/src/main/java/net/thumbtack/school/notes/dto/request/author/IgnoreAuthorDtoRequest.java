package net.thumbtack.school.notes.dto.request.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validator.author.ValidLogin;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class IgnoreAuthorDtoRequest {
    @ValidLogin
    private String login;
}
