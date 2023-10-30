package com.knj.mirou.boundedContext.challengefeed.entity;

import com.knj.mirou.boundedContext.challenge.entity.Challenge;
import com.knj.mirou.boundedContext.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class ChallengeFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge linkedChallenge;

    private LocalDateTime createdDate;

    private String feedContents;

    private int likeCount;

//    private Photo photo;
}
