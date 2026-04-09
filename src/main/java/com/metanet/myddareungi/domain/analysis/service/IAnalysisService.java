package com.metanet.myddareungi.domain.analysis.service;

import java.util.List;

import com.metanet.myddareungi.domain.analysis.dto.AnalysisResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisSearchRequest;

public interface IAnalysisService {
    List<AnalysisResponse> searchAnalysis(AnalysisSearchRequest request);
}
