package com.knj.mirou.base.security.token.repository;

import com.knj.mirou.base.security.token.entity.RememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;

public class JpaPersistentTokenRepository implements PersistentTokenRepository {

    private final RememberMeTokenRepository rememberMeTokenRepository;

    public JpaPersistentTokenRepository(final RememberMeTokenRepository repository) {
        this.rememberMeTokenRepository = repository;
    }

    @Override
    public void createNewToken(final PersistentRememberMeToken token) {
        rememberMeTokenRepository.save(RememberMeToken.from(token));
    }

    @Override
    public void updateToken(final String series, final String tokenValue, final Date lastUsed) {
        rememberMeTokenRepository.findBySeries(series)
                .ifPresent(rememberMeToken -> {
                    rememberMeToken.updateToken(tokenValue, lastUsed);
                    rememberMeTokenRepository.save(rememberMeToken);
                });
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(final String seriesId) {
        return rememberMeTokenRepository.findBySeries(seriesId)
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
        rememberMeTokenRepository.deleteAllInBatch(rememberMeTokenRepository.findByUsername(username));
    }
}
