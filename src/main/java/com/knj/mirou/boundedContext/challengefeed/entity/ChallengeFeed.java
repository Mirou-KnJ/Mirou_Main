package com.knj.mirou.boundedContext.challengefeed.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    private String contents;

    @Builder.Default
    private int likeCount = 0;

//    private Photo photo;
}
