package com.knj.mirou.base.initData;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.config.LabelConfig;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeLabel;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
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

    private static final String DEFAULT_IMG_URL = "https://kr.object.ncloudstorage.com/mirou/etc/no_img.png";

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChallengeService challengeService,
            PublicRewardService publicRewardService,
            ImageDataService imageDataService,
            LabelConfig labelConfig
    ){
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run (String... args) throws Exception {

                labelConfig.setLabels();

                if(memberService.getByLoginId("TEST_USER_1").isPresent()) {
                    return;
                }

                for(int i=1; i<=3; i++) {

                    LocalDate date = LocalDate.now().plusDays(30);
                    ChallengeCreateDTO createDto = ChallengeCreateDTO.builder()
                            .name("테스트 챌린지 %d".formatted(i))
                            .contents("테스트 챌린지 %d의 내용입니다.".formatted(i))
                            .level(3)
                            .tag("ETC")
                            .joinDeadLine(date)
                            .precaution("주의사항 입니다.")
                            .period(7)
                            .joinCost(1000)
                            .method("PHOTO")
                            .labelList("LABEL1,LABEL2,LABEL3")
                            .build();

                    memberService.join("ETC", "TEST_USER_" + i, "테스트 유저" + i);
                    RsData<Challenge> createRs = challengeService.tryCreate(createDto, DEFAULT_IMG_URL);

                    createRs.printResult();

                    Challenge createdChallenge = createRs.getData();
                    imageDataService.create(createdChallenge.getId(), ImageTarget.CHALLENGE_IMG, DEFAULT_IMG_URL);
                }

                publicRewardService.create(1, 1, "COIN", "100");
                publicRewardService.create(1, 3, "COIN", "300");
                publicRewardService.create(1, 5, "COIN", "500");

                publicRewardService.create(2, 2, "COIN", "200");
                publicRewardService.create(2, 4, "COIN", "400");
                publicRewardService.create(2, 6, "COIN", "600");

                publicRewardService.create(3, 1, "COIN", "100");
            }
        };
    }

}
