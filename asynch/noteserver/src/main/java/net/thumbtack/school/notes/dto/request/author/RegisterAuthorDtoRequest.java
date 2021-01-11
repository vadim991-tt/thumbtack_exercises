package net.thumbtack.school.notes.dto.request.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validator.author.*;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class RegisterAuthorDtoRequest {
    @ValidFirstName
    private String firstName;
    @ValidLastName
    private String lastName;
    @ValidPatronymic
    private String patronymic;
    @ValidLogin
    private String login;
    @ValidPassword
    private String password;
}
