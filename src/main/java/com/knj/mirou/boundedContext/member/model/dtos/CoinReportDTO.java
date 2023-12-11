package com.knj.mirou.boundedContext.member.model.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@RequiredArgsConstructor
public class CoinReportDTO {
    public int weeklyGivenCoinSum;
    public int weeklyUsedCoinSum;

    public int weeklyGivenCoinAverage;
    public int weeklyUsedCoinAverage;

    public int topCoinUserAmount;
}
