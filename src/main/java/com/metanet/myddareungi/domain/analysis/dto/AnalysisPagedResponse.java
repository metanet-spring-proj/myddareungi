package com.metanet.myddareungi.domain.analysis.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AnalysisPagedResponse {
    private List<AnalysisResponse> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
