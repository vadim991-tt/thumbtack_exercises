package net.thumbtack.school.notes.dto.request.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validator.author.ValidFirstName;
import net.thumbtack.school.notes.validator.author.ValidLastName;
import net.thumbtack.school.notes.validator.author.ValidPassword;
import net.thumbtack.school.notes.validator.author.ValidPatronymic;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class ChangePasswordDtoRequest {
    @ValidFirstName
    private String firstName;
    @ValidLastName
    private String lastName;
    @ValidPatronymic
    private String patronymicName;
    @ValidPassword
    private String oldPassword;
    @ValidPassword
    private String newPassword;
}
