package com.knj.mirou.base.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.io.Serializable;
import java.util.Date;

@Getter
@Entity
@Table(name = "persistent_logins")
public class PersistentLogin implements Serializable {

    @Id
    private String series;

    private String username;

    private String token;

    private Date lastUsed;

    protected PersistentLogin() {
    }

    public PersistentLogin(final PersistentRememberMeToken token) {
        this.series = token.getSeries();
        this.username = token.getUsername();
        this.token = token.getTokenValue();
        this.lastUsed = token.getDate();
    }

    public static PersistentLogin from(final PersistentRememberMeToken token) {
        return new PersistentLogin(token);
    }

    public void updateToken(final String tokenValue, final Date lastUsed) {
        this.token = tokenValue;
        this.lastUsed = lastUsed;
    }
}
