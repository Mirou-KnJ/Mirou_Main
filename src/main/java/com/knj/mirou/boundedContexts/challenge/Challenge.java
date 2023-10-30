package com.knj.mirou.boundedContexts.challenge;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {
    @Id

    private long id;
    private String challengeName; //챌린지 이름
    private String challengeContents; //챌린지 내용
    private String reward; //보상
    private Date joinDeadLine; //마감 기간
    private int challengePeriod; //진행 기간
    private boolean status; //진행 상태
    private int level; //챌린지 예상 난이도
    //챌린지 이미지
}
