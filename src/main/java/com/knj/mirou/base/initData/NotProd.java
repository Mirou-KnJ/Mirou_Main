package com.knj.mirou.base.initData;

import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.member.service.MemberService;
import com.knj.mirou.boundedContext.reward.service.PublicRewardService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
@Profile({"dev", "test"})
public class NotProd {

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChallengeService challengeService,
            PublicRewardService publicRewardService
    ){
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run (String... args) throws Exception {

                for(int i=1; i<=3; i++) {
                    memberService.join("ETC", "TEST_USER_" + i, "테스트 유저" + i);
                    challengeService.create("테스트 챌린지 " + i, "테스트 챌린지 " + i + "의 내용입니다.",
                            1000, LocalDate.now(), 7, "ETC", "PHOTO",
                            3, "테스트 챌린지 주의사항 입니다.");
                }

                publicRewardService.create(1, 1, "COIN", "100");
                publicRewardService.create(1, 3, "COIN", "300");
                publicRewardService.create(1, 5, "COIN", "500");

                publicRewardService.create(2, 2, "COIN", "200");
                publicRewardService.create(2, 4, "COIN", "400");
                publicRewardService.create(2, 6, "COIN", "600");

                publicRewardService.create(3, 3, "COIN", "100");
                publicRewardService.create(3, 5, "COIN", "500");
                publicRewardService.create(3, 7, "COIN", "1000");
            }
        };
    }

}
