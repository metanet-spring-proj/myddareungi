package com.metanet.myddareungi.domain.analysis.service;

import com.metanet.myddareungi.domain.analysis.dto.AnalysisPagedResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisSearchRequest;
import com.metanet.myddareungi.domain.analysis.model.AnalysisResult;
import com.metanet.myddareungi.domain.analysis.repository.AnalysisRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisService implements IAnalysisService {

    private final AnalysisRepository analysisRepository;

    @Override
    public AnalysisPagedResponse searchAnalysis(AnalysisSearchRequest request) {

        int pageSize = 12;
        int page = request.getPage() == null ? 1 : request.getPage();

        int offset = (page - 1) * pageSize;
        
        request.setOffset(offset);
        request.setPageSize(pageSize);

        List<AnalysisResult> results = analysisRepository.searchAnalysis(request);
        int totalElements = analysisRepository.countSearchAnalysis(request);
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        List<AnalysisResponse> content = results.stream()
                .map(r -> AnalysisResponse.builder()
                        .stationName(r.getStationName())
                        .district(r.getDistrict())
                        .month(r.getMonth())
                        .ageGroup(r.getAgeGroup())
                        .totalUseCnt(r.getTotalUseCnt())
                        .build())
                .collect(Collectors.toList());

        return AnalysisPagedResponse.builder()
                .content(content)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .currentPage(page)
                .build();
    }

    @Override
    public List<AnalysisResponse> listAllAnalysis(AnalysisSearchRequest request) {
        List<AnalysisResult> results = analysisRepository.listAllAnalysis(request);
        return results.stream()
                .map(r -> AnalysisResponse.builder()
                        .stationName(r.getStationName())
                        .district(r.getDistrict())
                        .month(r.getMonth())
                        .ageGroup(r.getAgeGroup())
                        .totalUseCnt(r.getTotalUseCnt())
                        .build())
                .collect(Collectors.toList());
    }
}