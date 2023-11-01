package com.knj.mirou.boundedContext.challenge.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengePeriod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
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

    private LocalDate joinDeadline;

    @Enumerated(EnumType.STRING)
    private ChallengePeriod period;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    @Enumerated(EnumType.STRING)
    private ChallengeTag tag;

    @Enumerated(EnumType.STRING)
    private AuthenticationMethod method;

    private int requiredNum;    //총 몇 번 성공해야하는 챌린지인지.

    private int level;

    private int joinCost;

    private String precautions;
}
