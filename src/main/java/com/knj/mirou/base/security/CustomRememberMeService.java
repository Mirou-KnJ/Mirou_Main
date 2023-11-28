package com.knj.mirou.base.security;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.security.token.entity.PersistentLogin;
import com.knj.mirou.base.security.token.repository.PersistentLoginRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CustomRememberMeService implements RememberMeServices {

    private final UserDetailsService userDetailsService;
    private final PersistentLoginRepository persistentLoginRepository;
    private final MemberService memberService;
    private final Rq rq;

    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {

        Member member = rq.getMember();

        // Remember Me 토큰 생성
        String username = member.getNickname();
        String series = String.valueOf(UUID.randomUUID());
        String tokenValue = member.getLoginId();

        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, series, tokenValue, new Date());
        PersistentLogin persistentLogin = new PersistentLogin(persistentToken);

        // 토큰을 저장소에 저장
        persistentLoginRepository.save(persistentLogin);

        // Remember Me 쿠키 생성
        RememberMeAuthenticationToken rememberMeToken = new RememberMeAuthenticationToken("key",
                persistentToken, memberService.getGrantedAuthorities(username));
//        createRememberMeCookie(rememberMeToken, response);

        return rememberMeToken;
    }

    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {

        log.error("loginFail!!!!");
        return;
    }

    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {

        log.info("loginSuccess!!!!");
        return;
    }

//    private void createRememberMeCookie(RememberMeAuthenticationToken rememberMeToken, HttpServletResponse response) {
//        // Remember Me 토큰을 쿠키에 추가
//        RememberMeAuthenticationFilter
//                .makeTokenSignature(rememberMeToken, System.currentTimeMillis(), "yourSecretKey")
//                .ifPresent(signature -> response.addCookie(
//                        new Cookie(RememberMeConfigurer.DEFAULT_REMEMBER_ME_COOKIE_NAME, signature))
//                );
//    }
}
