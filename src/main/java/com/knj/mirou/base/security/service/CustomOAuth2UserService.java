package com.knj.mirou.base.security.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = oAuth2User.getName();
        String socialCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String loginId = socialCode + "__%s".formatted(oauthId);
        String nickname = socialCode + "__%s".formatted(oauthId);

        RsData<Member> socialLoginRs = memberService.socialLogin(socialCode, loginId, nickname);

        if (socialLoginRs.isFail()) {
            return null;            //FIXME
        }

        Member member = socialLoginRs.getData();

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
