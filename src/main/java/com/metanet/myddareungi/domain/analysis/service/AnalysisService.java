package com.metanet.myddareungi.domain.analysis.service;

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
    public List<AnalysisResponse> searchAnalysis(AnalysisSearchRequest request) {

        int pageSize = 12;
        int page = request.getPage() == null ? 1 : request.getPage();

        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        request.setStartRow(startRow);
        request.setEndRow(endRow);

        List<AnalysisResult> results = analysisRepository.searchAnalysis(request);

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