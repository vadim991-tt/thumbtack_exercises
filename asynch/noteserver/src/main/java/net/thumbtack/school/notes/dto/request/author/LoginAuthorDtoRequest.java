package net.thumbtack.school.notes.dto.request.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validator.author.ValidLogin;
import net.thumbtack.school.notes.validator.author.ValidPassword;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class LoginAuthorDtoRequest {
    @ValidLogin
    private String login;
    @ValidPassword
    private String password;
}
