package com.metanet.myddareungi.domain.analysis.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.metanet.myddareungi.domain.analysis.dto.AnalysisPagedResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisSearchRequest;
import com.metanet.myddareungi.domain.analysis.service.IAnalysisService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "Analysis", description = "따릉이 데이터 상세 분석 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/analysis")
public class AnalysisController {

    private final IAnalysisService analysisService;

    @Operation(summary = "상세 데이터 검색", description = "월, 자치구, 연령대 필터를 사용하여 따릉이 이용 통계를 조회합니다.")
    @GetMapping("/search")
    public String searchAnalysis(

            @ModelAttribute("request") AnalysisSearchRequest request,
            Model model) {

        if (request.getPage() == null || request.getPage() <= 0) {
            request.setPage(1);
        }

        // System.out.println("========== 검색 필터 로그 ==========");
        // System.out.println("선택된 월(Month): " + request.getMonth());
        // System.out.println("선택된 구(District): " + request.getDistrict());
        // System.out.println("선택된 연령대(AgeGroup): " + request.getAgeGroup());
        // System.out.println("현재 페이지: " + request.getPage());
        // System.out.println("===================================");

        AnalysisPagedResponse pagedResponse = analysisService.searchAnalysis(request);

        model.addAttribute("result", pagedResponse.getContent());
        model.addAttribute("totalElements", pagedResponse.getTotalElements());
        model.addAttribute("totalPages", pagedResponse.getTotalPages());
        model.addAttribute("currentPage", pagedResponse.getCurrentPage());
        model.addAttribute("activeMenu", "analysis");

        return "analysis/analysis";
    }

    @Operation(summary = "분석 데이터 CSV 내보내기", description = "현재 필터링된 모든 분석 데이터를 CSV 파일로 다운로드합니다.")
    @GetMapping("/export/csv")
    public void exportCsv(

            @ModelAttribute("request") AnalysisSearchRequest request,
            HttpServletResponse response) throws IOException {

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