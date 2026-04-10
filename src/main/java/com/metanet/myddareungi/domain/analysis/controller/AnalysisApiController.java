package com.metanet.myddareungi.domain.analysis.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.metanet.myddareungi.domain.analysis.dto.AnalysisResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisSearchRequest;
import com.metanet.myddareungi.domain.analysis.service.IAnalysisService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "Analysis API", description = "따릉이 데이터 상세 분석을 위한 REST API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analysis")
public class AnalysisApiController {

    private final IAnalysisService analysisService;

    @Operation(summary = "분석 데이터 CSV 내보내기", description = "현재 필터링된 모든 분석 데이터를 CSV 파일로 다운로드합니다.")
    @GetMapping("/export/csv")
    public void exportCsv(
            @ModelAttribute("request") AnalysisSearchRequest request,
            Principal principal,
            HttpServletResponse response) throws IOException {

        if (principal != null) {
             // System.out.println("JWT 인증을 통한 CSV 다운로드 시도: " + principal.getName());
        }

        List<AnalysisResponse> allData = analysisService.listAllAnalysis(request);

        String fileName = URLEncoder.encode("따릉이_통계_데이터.csv", StandardCharsets.UTF_8).replace("+", "%20");
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (PrintWriter writer = response.getWriter()) {
            // Excel UTF-8 BOM
            writer.write("\uFEFF");
            writer.println("대여소 명,자치구,월,연령대,이용 건수");

            for (AnalysisResponse row : allData) {
                writer.printf("\"%s\",\"%s\",%d,\"%s\",%d%n",
                        row.getStationName(),
                        row.getDistrict(),
                        row.getMonth(),
                        row.getAgeGroup(),
                        row.getTotalUseCnt());
            }
        }
    }
}
