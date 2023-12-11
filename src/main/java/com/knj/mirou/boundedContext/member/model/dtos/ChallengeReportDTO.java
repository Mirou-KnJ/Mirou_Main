package com.knj.mirou.boundedContext.member.model.dtos;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@Setter
@Getter
@RequiredArgsConstructor
public class ChallengeReportDTO {

    public List<Challenge> openedChallenges;
    public Map<Long, Integer> writeCounts;

}
