package com.metanet.myddareungi.domain.analysis.dto;

import lombok.Builder;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Builder
public class AnalysisResponse {
    @Schema(description = "대여소 명", example = "한강버스 뚝섬 선착장")
    private String stationName;
    @Schema(description = "자치구", example = "마포구")
    private String district;
    @Schema(description = "조회 월", example = "4")
    private Integer month;
    @Schema(description = "연령대", example = "20대")
    private String ageGroup;
    @Schema(description = "총 이용 건수", example = "1540")
    private Integer totalUseCnt;

}
