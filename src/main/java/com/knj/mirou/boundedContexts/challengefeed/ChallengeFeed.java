package com.knj.mirou.boundedContexts.challengefeed;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeFeed {
    @Id

    private long id;
    private String writerId; //작성자
    private long linkedChallengeId; //챌린지 아이디 연결
    private Date createdAt; //작성 시간
    private String feedContents; //피드 게시글
    private int like; //좋아요
    //feed_image
}
