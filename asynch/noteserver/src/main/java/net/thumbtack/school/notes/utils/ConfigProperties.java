package net.thumbtack.school.notes.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Configuration("UserData")
public class ConfigProperties {
    private int maxNameLength;
    private int minPasswordLength;
    private int user_idle_timeout;

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    public int getUser_idle_timeout() {
        return user_idle_timeout;
    }

    public void setUser_idle_timeout(int user_idle_timeout) {
        this.user_idle_timeout = user_idle_timeout;
    }


}
