package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
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
    @DisplayName("t001")
    void t001() {

        ChallengeCreateDTO challengeCreateDTO = new ChallengeCreateDTO();
        challengeCreateDTO.setName("123");
        challengeCreateDTO.setJoinDeadLine(LocalDate.now().minusDays(1));

        RsData<Long> createRs = challengeService.create(challengeCreateDTO, TEST_CHALLENGE_IMG);

        assertThat(createRs.getResultCode()).contains("F");
    }

}