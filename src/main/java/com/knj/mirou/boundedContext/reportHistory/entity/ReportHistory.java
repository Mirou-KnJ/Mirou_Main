package com.knj.mirou.boundedContext.reportHistory.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.format.DateTimeFormatter;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class ReportHistory extends BaseEntity {

    @ManyToOne
    private ChallengeFeed targetFeed;

    @ManyToOne
    private Member reporter;

    @ManyToOne
    private Member reportedMember;

    private String contents;

    public String getCreateDateFormattedStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분");
        return formatter.format(getCreateDate());
    }
}
