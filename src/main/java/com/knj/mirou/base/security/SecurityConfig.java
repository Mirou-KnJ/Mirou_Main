package com.knj.mirou.base.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

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
                                .tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService)
                )

        ;

        return http.build();
    }

}
