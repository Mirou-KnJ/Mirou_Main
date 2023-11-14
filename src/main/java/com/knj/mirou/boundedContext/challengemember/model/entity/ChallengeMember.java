package com.knj.mirou.boundedContext.challengemember.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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

    private int successNumber;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    private LocalDateTime endDate;

    public void success() {
        this.successNumber = this.successNumber + 1;
    }

}
