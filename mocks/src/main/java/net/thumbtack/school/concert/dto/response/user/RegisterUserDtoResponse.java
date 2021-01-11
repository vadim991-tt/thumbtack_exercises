package net.thumbtack.school.concert.dto.response.user;

import java.util.UUID;

public class RegisterUserDtoResponse {
    private String token;

    public RegisterUserDtoResponse(UUID token) {
        this.token = token.toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
