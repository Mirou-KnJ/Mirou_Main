package com.knj.mirou.boundedContext.challengemember.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import com.knj.mirou.boundedContext.point.entity.Point;
import com.knj.mirou.boundedContext.point.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ChallengeMemberServiceTest {

    @Autowired
    private ChallengeMemberService challengeMemberService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private PointService pointService;

    private Member testMember1;
    private Member testMember2;
    private Challenge testChallenge1;
    private Challenge testChallenge2;

    @BeforeEach
    void setUp() {

        Optional<Member> OMember1 = memberService.getById(1L);
        if(OMember1.isPresent()) {
            testMember1 = OMember1.get();
        }

        Optional<Member> OMember2 = memberService.getById(2L);
        if(OMember2.isPresent()) {
            testMember2 = OMember2.get();
        }

        Optional<Challenge> OChallenge1 = challengeService.getById(3L);
        if(OChallenge1.isPresent()) {
            testChallenge1 = OChallenge1.get();
        }

        Optional<Challenge> OChallenge2 = challengeService.getById(2L);
        if(OChallenge2.isPresent()) {
            testChallenge2 = OChallenge2.get();
        }
    }

    @Test
    @DisplayName("포인트가 부족한 경우 참여가 불가능")
    void t001() {

        Point testMemberPoint = testMember1.getPoint();
        pointService.usingPoint(testMemberPoint, 3000);

        RsData<Long> challengeJoinRs1 = challengeMemberService.join(testChallenge1, testMember1);
        assertThat(challengeJoinRs1.isFail()).isTrue();
        assertThat(challengeJoinRs1.getResultCode()).startsWith("F");

        RsData<Long> challengeJoinRs2 = challengeMemberService.join(testChallenge1, testMember2);
        assertThat(challengeJoinRs2.isSuccess()).isTrue();
        assertThat(challengeJoinRs2.getResultCode()).startsWith("S");
    }

}