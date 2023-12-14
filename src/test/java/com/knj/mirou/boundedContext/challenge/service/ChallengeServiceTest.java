package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ChallengeServiceTest {

    @Autowired
    private ChallengeService challengeService;

    private static final String TEST_CHALLENGE_IMG = "https://kr.object.ncloudstorage.com/mirou/etc/system_noti.png";

    @Test
    @DisplayName("챌린지 생성시 종료 기한이 적합하지 않으면 생성 불가")
    void t001() {
        ChallengeCreateDTO testCreateDTO = new ChallengeCreateDTO();
        testCreateDTO.setJoinDeadLine(LocalDate.now().minusDays(1));

        RsData<Long> createRs = challengeService.create(testCreateDTO, TEST_CHALLENGE_IMG);

        assertThat(createRs.isFail()).isTrue();
        assertThat(createRs.getResultCode()).isEqualTo("F-1");
    }

    @Test
    @DisplayName("챌린지 생성시 중복된 이름의 챌린지가 있다면 생성 불가")
    void t002() {
        ChallengeCreateDTO testCreateDTO = new ChallengeCreateDTO();
        testCreateDTO.setJoinDeadLine(LocalDate.now().plusDays(10));
        testCreateDTO.setName("테스트 챌린지 1");

        RsData<Long> createRs = challengeService.create(testCreateDTO, TEST_CHALLENGE_IMG);

        assertThat(createRs.isFail()).isTrue();
        assertThat(createRs.getResultCode()).isEqualTo("F-2");
    }
}