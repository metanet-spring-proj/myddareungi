package com.metanet.myddareungi.domain.analysis.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisSearchRequest {
    @Schema(description = "조회 월 (1-12)", example = "4")
    private Integer month;

    @Schema(description = "자치구 목록 (중복 선택 가능)", example = "['강남구', '송파구']")
    private List<String> district;

    @Schema(description = "연령대 목록", example = "['20대', '30대']")
    private List<String> ageGroup;


    // pagination
    @Schema(description = "현재 페이지 번호", defaultValue = "1", example = "1")
    private Integer page = 1;


    // mapper 전달용
    @Schema(hidden = true)
    private Integer offset;
    @Schema(hidden = true)
    private Integer pageSize;

}
