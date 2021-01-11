package net.thumbtack.school.notes.dto.response.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class ServerSettingsDtoResponse {
    private int maxNameLength;
    private int minPasswordLength;
    private int user_idle_timeout;
}
