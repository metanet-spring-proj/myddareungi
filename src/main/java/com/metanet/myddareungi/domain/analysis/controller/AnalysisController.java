package com.metanet.myddareungi.domain.analysis.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.metanet.myddareungi.domain.analysis.dto.AnalysisResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisSearchRequest;
import com.metanet.myddareungi.domain.analysis.service.IAnalysisService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class AnalysisController {

    private final IAnalysisService analysisService;

    @GetMapping("/search")
    public String searchAnalysis(
            @ModelAttribute AnalysisSearchRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {
        request.setPage(page);

        List<AnalysisResponse> result = analysisService.searchAnalysis(request);

        model.addAttribute("result", result);
        model.addAttribute("request", request);

        return "analysis/analysis";
    }
}