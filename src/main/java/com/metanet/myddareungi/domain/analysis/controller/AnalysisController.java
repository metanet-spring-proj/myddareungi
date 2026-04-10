package com.metanet.myddareungi.domain.analysis.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.metanet.myddareungi.domain.analysis.dto.AnalysisPagedResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisSearchRequest;
import com.metanet.myddareungi.domain.analysis.service.IAnalysisService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Analysis View", description = "따릉이 데이터 상세 분석 뷰")
@Controller
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class AnalysisController {

    private final IAnalysisService analysisService;

    @Operation(summary = "상세 데이터 검색", description = "월, 자치구, 연령대 필터를 사용하여 따릉이 이용 통계를 조회합니다.")
    @GetMapping("/search")
    public String searchAnalysis(
            @ModelAttribute("request") AnalysisSearchRequest request,
            Principal principal,
            Model model) {

        if (principal != null) {
            model.addAttribute("username", principal.getName());
            // System.out.println("JWT 인증 확인: " + principal.getName());
        }


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
}