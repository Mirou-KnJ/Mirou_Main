package com.knj.mirou.base.security.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();

        String registrationId = clientRegistration.getRegistrationId();
        System.out.println("registrationId = " + registrationId);
        String clientId = clientRegistration.getClientId();
        System.out.println("clientId = " + clientId);
        String clientName = clientRegistration.getClientName();
        System.out.println("clientName = " + clientName);
        String clientSecret = clientRegistration.getClientSecret();
        System.out.println("clientSecret = " + clientSecret);
        String value = clientRegistration.getClientAuthenticationMethod().getValue();
        System.out.println("value = " + value);
        String value1 = clientRegistration.getAuthorizationGrantType().getValue();
        System.out.println("value1 = " + value1);
        ClientRegistration.ProviderDetails providerDetails = clientRegistration.getProviderDetails();
        String authorizationUri = providerDetails.getAuthorizationUri();
        System.out.println("authorizationUri = " + authorizationUri);
        String uri = providerDetails.getUserInfoEndpoint().getUri();
        System.out.println("uri = " + uri);
        String issuerUri = providerDetails.getIssuerUri();
        System.out.println("issuerUri = " + issuerUri);
        String tokenUri = providerDetails.getTokenUri();
        System.out.println("tokenUri = " + tokenUri);
        String string = providerDetails.getConfigurationMetadata().toString();
        System.out.println("string = " + string);
        String jwkSetUri = providerDetails.getJwkSetUri();
        System.out.println("jwkSetUri = " + jwkSetUri);

        String value2 = accessToken.getTokenType().getValue();
        System.out.println("value2 = " + value2);
        String tokenValue = accessToken.getTokenValue();
        System.out.println("tokenValue = " + tokenValue);
        String string1 = accessToken.getScopes().toString();
        System.out.println("string1 = " + string1);
        String string2 = accessToken.toString();
        System.out.println("string2 = " + string2);

        String string3 = additionalParameters.toString();
        System.out.println("string3 = " + string3);

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
