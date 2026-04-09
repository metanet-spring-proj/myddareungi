package com.metanet.myddareungi.domain.analysis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisResponse {
    private String stationName;
    private String district;
    private Integer month;
    private String ageGroup;
    private Integer totalUseCnt;
}
