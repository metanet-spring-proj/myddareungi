package com.metanet.myddareungi.domain.analysis.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisSearchRequest {
    private Integer month;
    private List<String> district;
    private List<String> ageGroup;

    // pagination
    private Integer page = 1;

    // mapper 전달용
    private Integer startRow;
    private Integer endRow;
}
