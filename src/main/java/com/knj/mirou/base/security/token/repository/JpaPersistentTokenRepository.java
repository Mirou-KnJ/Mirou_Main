package com.knj.mirou.base.security.token.repository;

import com.knj.mirou.base.security.token.entity.PersistentLogin;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;

public class JpaPersistentTokenRepository implements PersistentTokenRepository {

    private final PersistentLoginRepository persistentLoginRepository;

    public JpaPersistentTokenRepository(final PersistentLoginRepository repository) {
        this.persistentLoginRepository = repository;
    }

    @Override
    public void createNewToken(final PersistentRememberMeToken token) {
        persistentLoginRepository.save(PersistentLogin.from(token));
    }

    @Override
    public void updateToken(final String series, final String tokenValue, final Date lastUsed) {
        persistentLoginRepository.findBySeries(series)
                .ifPresent(rememberMeToken -> {
                    rememberMeToken.updateToken(tokenValue, lastUsed);
                    persistentLoginRepository.save(rememberMeToken);
                });
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(final String seriesId) {
        return persistentLoginRepository.findBySeries(seriesId)
                .map(rememberMeToken ->
                        new PersistentRememberMeToken(
                                rememberMeToken.getUsername(),
                                rememberMeToken.getSeries(),
                                rememberMeToken.getToken(),
                                rememberMeToken.getLastUsed()
                        ))
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void removeUserTokens(final String username) {
        persistentLoginRepository.deleteAllInBatch(persistentLoginRepository.findByUsername(username));
    }
}
