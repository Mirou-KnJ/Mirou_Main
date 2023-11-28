package com.knj.mirou.base.security;

import com.knj.mirou.base.security.token.repository.JpaPersistentTokenRepository;
import com.knj.mirou.base.security.token.repository.RememberMeTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PersistentTokenRepository tokenRepository;

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/member/login")
                                .defaultSuccessUrl("/")
                )
                .logout(
                        (logout) -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                )
                .rememberMe(
                        (rememberMe) -> rememberMe
                                .key("key")
                                .userDetailsService(customUserDetailsService)
                                .tokenRepository(tokenRepository)

                );

        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(final RememberMeTokenRepository repository) {
        return new JpaPersistentTokenRepository(repository);
    }

}
