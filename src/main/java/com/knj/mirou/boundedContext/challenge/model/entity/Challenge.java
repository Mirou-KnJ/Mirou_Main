package com.knj.mirou.boundedContext.challenge.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengePeriod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.reward.model.entity.Reward;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
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
public class Challenge extends BaseEntity {

    private String name;

    private String contents;

    @OneToMany(mappedBy = "linkedChallenge")
    private List<Reward> reward;

    private LocalDate joinDeadLine;

    @Enumerated(EnumType.STRING)
    private ChallengePeriod period;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    //총 몇 번 성공해야하는 챌린지인지.

    private int

    private int level;

//    private Photo photo;
}
