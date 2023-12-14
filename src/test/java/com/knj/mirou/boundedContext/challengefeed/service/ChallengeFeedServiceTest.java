package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ChallengeFeedServiceTest {

    @Autowired
    private ChallengeFeedService challengeFeedService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeMemberService challengeMemberService;

    private Member testMember1;
    private Challenge testChallenge1;

    private MultipartFile imgFile;
    private MultipartFile txtFile;

    @BeforeEach
    void setUp() {

        Optional<Member> OMember = memberService.getById(1L);
        if(OMember.isPresent()) {
            testMember1 = OMember.get();
        }

        Optional<Challenge> OChallenge = challengeService.getById(1L);
        if(OChallenge.isPresent()) {
            testChallenge1 = OChallenge.get();
        }

        Path imgFilePath = Paths.get("src/main/resources/static/img/test_water.png");
        try {
            byte[] fileBytes = Files.readAllBytes(imgFilePath);
            imgFile = new MockMultipartFile(
                    "sample.png",
                    "sample.png",
                    "image/png",
                    fileBytes
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        Path txtFilePath = Paths.get("src/main/resources/static/img/sampleTXT.txt");
        try {
            byte[] fileBytes = Files.readAllBytes(txtFilePath);
            txtFile = new MockMultipartFile(
                    "sample.txt",
                    "sample.txt",
                    "text/plain",
                    fileBytes
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("참여하지 않은 챌린지에는 인증글 작성이 불가")
    void t001() throws IOException {

        RsData<String> writeRs1 =
                challengeFeedService.write(testChallenge1, testMember1, imgFile, "물 마시기 인증");

        assertThat(writeRs1.isFail()).isTrue();
        assertThat(writeRs1.getResultCode()).startsWith("F");

        challengeMemberService.join(testChallenge1, testMember1);

        RsData<String> writeRs2 =
                challengeFeedService.write(testChallenge1, testMember1, imgFile, "물 마시기 인증");

        assertThat(writeRs2.isSuccess()).isTrue();
        assertThat(writeRs2.getResultCode()).startsWith("S");
    }
}