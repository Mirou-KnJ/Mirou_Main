package com.knj.mirou.boundedContext.challengefeed.model.dtos;

import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class FeedListDTO {

    private List<ChallengeFeed> myFeeds;
    private List<ChallengeFeed> notMineFeeds;

}
