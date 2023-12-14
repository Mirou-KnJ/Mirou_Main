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
    private Challenge testChallenge2;

    private MultipartFile waterImgFile;
    private MultipartFile notWaterImgFile;
    private MultipartFile txtFile;

    @BeforeEach
    void setUp() {

        Optional<Member> OMember = memberService.getById(1L);
        if(OMember.isPresent()) {
            testMember1 = OMember.get();
        }

        Optional<Challenge> OChallenge1 = challengeService.getById(3L);
        if(OChallenge1.isPresent()) {
            testChallenge1 = OChallenge1.get();
        }

        Optional<Challenge> OChallenge2 = challengeService.getById(2L);
        if(OChallenge2.isPresent()) {
            testChallenge2 = OChallenge2.get();
        }

        Path waterImgFilePath = Paths.get("src/main/resources/static/img/test_water.png");
        try {
            byte[] fileBytes = Files.readAllBytes(waterImgFilePath);
            waterImgFile = new MockMultipartFile(
                    "sample.png",
                    "sample.png",
                    "image/png",
                    fileBytes
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        Path notWaterImgFilePath = Paths.get("src/main/resources/static/img/not_water.png");
        try {
            byte[] fileBytes = Files.readAllBytes(notWaterImgFilePath);
            notWaterImgFile = new MockMultipartFile(
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
                challengeFeedService.write(testChallenge1, testMember1, waterImgFile, "물 마시기 인증");

        assertThat(writeRs1.isFail()).isTrue();
        assertThat(writeRs1.getResultCode()).startsWith("F");

        challengeMemberService.join(testChallenge1, testMember1);

        RsData<String> writeRs2 =
                challengeFeedService.write(testChallenge1, testMember1, waterImgFile, "물 마시기 인증");

        assertThat(writeRs2.isSuccess()).isTrue();
        assertThat(writeRs2.getResultCode()).startsWith("S");
    }

    @Test
    @DisplayName("이미지 파일이 아닌 경우 인증글 작성 불가")
    void t002() throws IOException {

        challengeMemberService.join(testChallenge1, testMember1);

        RsData<String> writeRs =
                challengeFeedService.write(testChallenge1, testMember1, txtFile, "물 마시기 인증");

        assertThat(writeRs.isFail()).isTrue();
        assertThat(writeRs.getResultCode()).startsWith("F");
    }

    @Test
    @DisplayName("챌린지에 맞는 이미지가 아니라면 인증글 작성 불가")
    void t003() throws IOException {

        challengeMemberService.join(testChallenge1, testMember1);

        RsData<String> writeRs1 =
                challengeFeedService.write(testChallenge1, testMember1, notWaterImgFile, "물 마시기 인증");

        assertThat(writeRs1.isFail()).isTrue();
        assertThat(writeRs1.getResultCode()).startsWith("F");

        RsData<String> writeRs2 =
                challengeFeedService.write(testChallenge1, testMember1, waterImgFile, "물 마시기 인증");

        assertThat(writeRs2.isSuccess()).isTrue();
        assertThat(writeRs2.getResultCode()).startsWith("S");
    }

    @Test
    @DisplayName("해당 회차에 맞는 보상이 존재한다면 보상 지급")
    void t004() throws IOException {

        int beforeCoin = testMember1.getCoin().getCurrentCoin();
        challengeMemberService.join(testChallenge1, testMember1);
        challengeFeedService.write(testChallenge1, testMember1, waterImgFile, "물 마시기 인증");
        int afterCoin = testMember1.getCoin().getCurrentCoin();

        //testChallenge1에는 1회차 보상이 존재
        assertThat(beforeCoin).isLessThan(afterCoin);

        int beforeCoin2 = testMember1.getCoin().getCurrentCoin();
        challengeMemberService.join(testChallenge2, testMember1);
        challengeFeedService.write(testChallenge2, testMember1, waterImgFile, "물 마시기 인증");
        int afterCoin2 = testMember1.getCoin().getCurrentCoin();

        //testChallenge2 에는 1회차 보상이 없음
        assertThat(beforeCoin2).isEqualTo(afterCoin2);
    }

    @Test
    @DisplayName("자신의 글에는 좋아요 처리가 불가능")
    public void t005() throws IOException {

        challengeMemberService.join(testChallenge1, testMember1);
        challengeFeedService.write(testChallenge1, testMember1, notWaterImgFile, "물 마시기 인증");

        RsData<Integer> likeRs =
                challengeFeedService.updateLikeCount(1L, testMember1);

        assertThat(likeRs.isFail()).isTrue();
        assertThat(likeRs.getResultCode()).startsWith("F");
    }

}