package net.thumbtack.school.concert.dto.response.user;

import java.util.UUID;

public class LoginUserDtoResponse {
    private String token;

    public LoginUserDtoResponse(UUID token) {
        this.token = token.toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
