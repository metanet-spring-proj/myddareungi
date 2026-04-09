package com.metanet.myddareungi.domain.dashboard.dto;

import java.util.List;

import lombok.Data;

@Data
public class BikeWeekdaySummaryResponseDto {
    private List<String> weekdayList;
    private List<Long> useCountList;
}