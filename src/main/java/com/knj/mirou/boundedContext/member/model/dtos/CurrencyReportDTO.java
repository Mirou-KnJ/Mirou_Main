package com.knj.mirou.boundedContext.member.model.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@RequiredArgsConstructor
public class CurrencyReportDTO {
    public int weeklyGivenCurrencySum;
    public int weeklyUsedCurrencySum;

    public long weeklyGivenCurrencyAverage;
    public long weeklyUsedCurrencyAverage;
}
