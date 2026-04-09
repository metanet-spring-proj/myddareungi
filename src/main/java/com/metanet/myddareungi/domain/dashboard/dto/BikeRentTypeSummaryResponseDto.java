package com.metanet.myddareungi.domain.dashboard.dto;

import java.util.List;

import lombok.Data;

@Data
public class BikeRentTypeSummaryResponseDto {
    private List<String> rentTypeList;
    private List<Long> useCountList;
}