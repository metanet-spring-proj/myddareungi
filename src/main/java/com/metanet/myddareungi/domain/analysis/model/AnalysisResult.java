package com.metanet.myddareungi.domain.analysis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisResult {
    private String stationName;
    private String district;
    private Integer month;
    private String ageGroup;
    private Integer totalUseCnt;
}
