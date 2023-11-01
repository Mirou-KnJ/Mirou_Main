package com.knj.mirou.boundedContext.member.service;

import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.model.enums.MemberRole;
import com.knj.mirou.boundedContext.member.model.enums.SocialCode;
import com.knj.mirou.boundedContext.member.repository.MemberRepository;
import com.knj.mirou.boundedContext.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PointService pointService;
    private final CoinService coinService;

    public Optional<Member> getByLoginId(String loginId) {

        return memberRepository.findByLoginId(loginId);
    }

    @Transactional
    public Map<String, String> join(String socialCode, String loginId) {

        Map<String, String> joinResultMap = new HashMap<>();

        if(getByLoginId(loginId).isPresent()) {
            joinResultMap.put("ResultCode", "F-1");
            joinResultMap.put("Msg", "이미 가입된 회원 입니다.");
            return joinResultMap;
        } else {

            Member member = Member.builder()
                    .loginId(loginId)
                    .socialCode(SocialCode.valueOf(socialCode))   //FIXME 로그인 경로에 따라 다르게 설정
                    .role(MemberRole.USER)          //FIXME 일반 회원 / 관리자 구분
                    .inviteCode("123456789")        //FIXME 난수 처리
                    .coin(coinService.createCoin())
                    .point(pointService.createPoint())
                    .build();

            memberRepository.save(member);

            joinResultMap.put("ResultCode", "S-1");
            joinResultMap.put("Msg", "회원 가입이 완료 되었습니다.");

            return joinResultMap;
        }
    }

}
