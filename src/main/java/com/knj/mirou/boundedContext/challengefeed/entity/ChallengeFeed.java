package com.knj.mirou.boundedContext.challengefeed.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challenge.entity.Challenge;
import com.knj.mirou.boundedContext.member.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class ChallengeFeed extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge linkedChallenge;

    private String feedContents;

    @Builder.Default
    private int likeCount = 0;

//    private Photo photo;
}
