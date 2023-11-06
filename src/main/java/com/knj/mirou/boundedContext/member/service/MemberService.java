package com.knj.mirou.boundedContext.member.service;

import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.member.config.MemberConfigProperties;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.model.enums.MemberRole;
import com.knj.mirou.boundedContext.member.model.enums.SocialCode;
import com.knj.mirou.boundedContext.member.repository.MemberRepository;
import com.knj.mirou.boundedContext.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PointService pointService;
    private final CoinService coinService;
    private final MemberConfigProperties memberConfigProps;

    public Optional<Member> getByLoginId(String loginId) {

        return memberRepository.findByLoginId(loginId);
    }

    @Transactional
    public Map<String, Object> join(String socialCode, String loginId, String nickname) {

        Map<String, Object> joinResultMap = new HashMap<>();

        Optional<Member> ObyLoginId = getByLoginId(loginId);

        if(ObyLoginId.isPresent()) {
            joinResultMap.put("ResultCode", "F-1");
            joinResultMap.put("Msg", "이미 가입된 회원 입니다.");
            joinResultMap.put("Data", ObyLoginId.get());
            return joinResultMap;
        } else {


            MemberRole role = isAdmin(loginId) ? MemberRole.ADMIN : MemberRole.USER;

            Member member = Member.builder()
                    .loginId(loginId)
                    .nickname(nickname)
                    .socialCode(SocialCode.valueOf(socialCode))   //FIXME 로그인 경로에 따라 다르게 설정
                    .role(role)
                    .inviteCode("123456789")                      //FIXME 난수 처리
                    .coin(coinService.createCoin())
                    .point(pointService.createPoint())
                    .build();

            Member joinMember = memberRepository.save(member);

            joinResultMap.put("ResultCode", "S-1");
            joinResultMap.put("Msg", "회원 가입이 완료 되었습니다.");
            joinResultMap.put("Data", joinMember);

            return joinResultMap;
        }
    }

    public Map<String, Object> socialLogin(String socialCode, String loginId, String nickname) {

        Map<String,Object> socialResultMap = new HashMap<>();

        Optional<Member> ObyLoginId = getByLoginId(loginId);

        if(ObyLoginId.isPresent()) {
            socialResultMap.put("ResultCode", "S-1");
            socialResultMap.put("Msg", "로그인 되었습니다(이미 가입한 회원)");
            socialResultMap.put("Data", ObyLoginId.get());
            return socialResultMap;
        }

        Map<String, Object> joinResultMap = join(socialCode, loginId, nickname);

        if(joinResultMap.get("ResultCode").toString().startsWith("S")) {
            socialResultMap.put("ResultCode", "S-2");
            socialResultMap.put("Msg", "로그인 되었습니다(새로 가입한 회원)");
            socialResultMap.put("Data", joinResultMap.get("Data"));
        } else {
            socialResultMap.put("ResultCode", "F-1");
            socialResultMap.put("Msg", "가입 및 로그인에 실패하였습니다.");
        }

        return socialResultMap;
    }

    public List<? extends GrantedAuthority> getGrantedAuthorities(String loginId) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        if (isAdmin(loginId)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return grantedAuthorities;
    }

    public boolean isAdmin(String loginId) {

        return memberConfigProps.isAdmin(loginId);
    }

}
