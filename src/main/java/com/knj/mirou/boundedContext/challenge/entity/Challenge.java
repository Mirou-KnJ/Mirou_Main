package com.knj.mirou.boundedContext.challenge.entity;

import com.knj.mirou.boundedContext.reward.Reward;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String contents;

    @OneToMany(mappedBy = "linkedChallenge")
    private List<Reward> reward;

    private LocalDate joinDeadLine;

    private ChallengePeriod period;

    private ChallengeStatus status;

    @CreatedDate
    private LocalDateTime createdDate;

    private int level;

//    private Photo photo;
}
