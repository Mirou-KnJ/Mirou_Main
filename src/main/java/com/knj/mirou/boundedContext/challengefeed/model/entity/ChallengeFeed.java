package com.knj.mirou.boundedContext.challengefeed.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengefeed.model.enums.FeedStatus;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.format.DateTimeFormatter;

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

    @Builder.Default
    private int reportCount = 0;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private FeedStatus status = FeedStatus.PUBLIC;

    private String imgUrl;

    public String getCreateDateFormatStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY년 MM월 dd일 HH시 mm분 인증");
        return formatter.format(getCreateDate());
    }

    public String getCreateDateShortsStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일 HH시 인증");
        return formatter.format(getCreateDate());
    }

    public void updateLikeCount() {
        this.likeCount = likeCount + 1;
    }

    public void updateReportCount() {
        this.reportCount = reportCount + 1;
    }

    public void updatePrivate() {
        this.status = FeedStatus.PRIVATE;
    }
}
