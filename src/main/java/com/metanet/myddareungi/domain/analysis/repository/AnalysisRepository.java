package com.metanet.myddareungi.domain.analysis.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.metanet.myddareungi.domain.analysis.model.AnalysisResult;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisSearchRequest;

@Mapper
public interface AnalysisRepository {
    List<AnalysisResult> searchAnalysis(AnalysisSearchRequest request);
    int countSearchAnalysis(AnalysisSearchRequest request);
    List<AnalysisResult> listAllAnalysis(AnalysisSearchRequest request);
}
