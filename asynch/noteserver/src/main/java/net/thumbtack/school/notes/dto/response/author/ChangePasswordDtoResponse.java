package net.thumbtack.school.notes.dto.response.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class ChangePasswordDtoResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
}
