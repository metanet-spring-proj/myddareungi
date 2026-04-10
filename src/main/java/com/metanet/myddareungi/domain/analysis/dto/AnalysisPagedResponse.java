package com.metanet.myddareungi.domain.analysis.dto;

import lombok.Builder;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.List;

@Getter
@Builder
public class AnalysisPagedResponse {
    @Schema(description = "데이터 목록")
    private List<AnalysisResponse> content;
    @Schema(description = "전체 데이터 수", example = "100")
    private long totalElements;
    @Schema(description = "전체 페이지 수", example = "10")
    private int totalPages;
    @Schema(description = "현재 페이지 번호", example = "1")
    private int currentPage;

}
