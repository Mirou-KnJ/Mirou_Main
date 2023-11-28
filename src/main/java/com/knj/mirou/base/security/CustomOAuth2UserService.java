package com.knj.mirou.base.security;

import com.google.auto.value.extension.serializable.SerializableAutoValue;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.base.security.token.entity.PersistentLogin;
import com.knj.mirou.base.security.token.repository.PersistentLoginRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final CustomUserDetailsService userDetailsService;
    private final PersistentLoginRepository persistentLoginRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = oAuth2User.getName();      //회원번호? 같은 느낌. 3143014618

        String socialCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();    //KAKAO

        String loginId = socialCode + "__%s".formatted(oauthId);
        String nickname = socialCode + "__%s".formatted(oauthId);

        RsData<Member> socialLoginRs = memberService.socialLogin(socialCode, loginId, nickname);

        if (socialLoginRs.isFail()) {
            return null;            //FIXME
        }

        Member member = socialLoginRs.getData();

        // Remember Me 토큰 생성
        String username = member.getNickname();
        String series = String.valueOf(UUID.randomUUID());
        String tokenValue = loginId;

        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, series, tokenValue, new Date());
        PersistentLogin persistentLogin = new PersistentLogin(persistentToken);

        // 토큰을 저장소에 저장
        persistentLoginRepository.save(persistentLogin);

        return new CustomOAuth2User(member.getLoginId(), member.getNickname(),
                memberService.getGrantedAuthorities(member.getLoginId()));
    }

    class CustomOAuth2User extends User implements OAuth2User {

        public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }

        @Override
        public Map<String, Object> getAttributes() {
            return null;
        }

        @Override
        public String getName() {
            return getUsername();
        }
    }

}
