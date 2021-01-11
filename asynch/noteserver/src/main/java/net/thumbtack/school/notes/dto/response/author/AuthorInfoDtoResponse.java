package net.thumbtack.school.notes.dto.response.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class AuthorInfoDtoResponse {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
}
