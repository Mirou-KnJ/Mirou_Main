package com.knj.mirou.boundedContext.challenge.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import com.knj.mirou.boundedContext.challenge.model.enums.MapCategory;
import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @OneToMany(mappedBy = "linkedChallenge", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<PublicReward> publicReward;

    private LocalDate joinDeadline;

    private int period;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    @Enumerated(EnumType.STRING)
    private ChallengeTag tag;

    @Enumerated(EnumType.STRING)
    private MapCategory mapCategory;

    @Enumerated(EnumType.STRING)
    private AuthenticationMethod method;

    @ElementCollection
    private List<String> labels;

    private int level;

    private int joinCost;

    private String precautions;

    private String imgUrl;

    public void openingChallenge() {
        this.status = ChallengeStatus.OPEN;
    }

    public void closingChallenge() {
        this.status = ChallengeStatus.CLOSE;
    }

    public String getJoinDeadLineFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd(E) 마감");
        return formatter.format(joinDeadline);
    }

    public int getRewardSum() {

        int sum = 0;
        for (PublicReward reward : publicReward) {
            sum += Integer.parseInt(reward.getReward());
        }

        return sum;
    }
}
