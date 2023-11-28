package com.knj.mirou.base.security.config;

import com.knj.mirou.base.security.token.repository.JpaPersistentTokenRepository;
import com.knj.mirou.base.security.token.repository.RememberMeTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class PersistentTokenConfig {

    @Bean
    public PersistentTokenRepository persistentTokenRepository(final RememberMeTokenRepository repository) {
        return new JpaPersistentTokenRepository(repository);
    }
}
