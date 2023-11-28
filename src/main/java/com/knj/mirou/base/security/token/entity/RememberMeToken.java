package com.knj.mirou.base.security.token.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.io.Serializable;
import java.util.Date;

@Getter
@Entity
public class RememberMeToken implements Serializable {

    @Id
    private String series;

    private String username;

    private String token;

    private Date lastUsed;

    protected RememberMeToken() {
    }

    private RememberMeToken(final PersistentRememberMeToken token) {
        this.series = token.getSeries();
        this.username = token.getUsername();
        this.token = token.getTokenValue();
        this.lastUsed = token.getDate();
    }

    public static RememberMeToken from(final PersistentRememberMeToken token) {
        return new RememberMeToken(token);
    }

    public void updateToken(final String tokenValue, final Date lastUsed) {
        this.token = tokenValue;
        this.lastUsed = lastUsed;
    }
}
