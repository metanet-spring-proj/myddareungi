package com.metanet.myddareungi.domain.analysis.service;

import java.util.List;

import com.metanet.myddareungi.domain.analysis.dto.AnalysisPagedResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisSearchRequest;

public interface IAnalysisService {
    AnalysisPagedResponse searchAnalysis(AnalysisSearchRequest request);
    List<AnalysisResponse> listAllAnalysis(AnalysisSearchRequest request);
}
