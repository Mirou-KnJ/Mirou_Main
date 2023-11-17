package com.knj.mirou.boundedContext.challenge.model.dtos;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
@RequiredArgsConstructor
public class ChallengeDetailDTO {

    private boolean isJoin;
    private boolean canJoin;
    private boolean canWrite;
    private int memberCount;
    private Member loginMember;
    private Challenge challenge;
    private List<PublicReward> publicRewards;
    private List<PrivateReward> privateRewards;
    private List<ChallengeFeed> recently3Feeds;

}
