package net.thumbtack.school.notes.dto.response.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class AuthorDtoResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String timeRegistered;
    private boolean online;
    private boolean deleted;
    private int rating;
}
