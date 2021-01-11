package net.thumbtack.school.notes.dto.response.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class RegisterAuthorDtoResponse {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
}
