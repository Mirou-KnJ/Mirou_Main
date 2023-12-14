package com.knj.mirou.base.security.config;

import com.knj.mirou.base.security.repository.JpaPersistentTokenRepository;
import com.knj.mirou.base.security.repository.PersistentLoginRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class PersistentTokenConfig {

    @Bean
    public PersistentTokenRepository persistentTokenRepository(final PersistentLoginRepository repository) {
        return new JpaPersistentTokenRepository(repository);
    }
}
