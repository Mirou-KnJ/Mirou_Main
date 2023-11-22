package com.knj.mirou.boundedContext.challengemember.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class ChallengeMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge linkedChallenge;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member linkedMember;

    @OneToMany(mappedBy = "linkedChallengeMember", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<PrivateReward> privateReward;

    private int successNumber;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    private LocalDate endDate;

    public int success() {
        this.successNumber = this.successNumber + 1;
        return successNumber;
    }

    public void finishChallenge() {
        this.progress = Progress.PROGRESS_END;
    }

    public int getLastDayNumber() {

        LocalDate today = LocalDate.now();
        int todayValue = (today.getYear() * 365) + (today.getMonthValue() * 12) + today.getDayOfMonth();
        int endDateValue = (endDate.getYear() * 365) + (endDate.getMonthValue() * 12) + endDate.getDayOfMonth();

        return endDateValue - todayValue;
    }

}
