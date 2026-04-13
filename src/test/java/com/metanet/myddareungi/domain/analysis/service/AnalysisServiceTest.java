package com.metanet.myddareungi.domain.analysis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.metanet.myddareungi.domain.analysis.dto.AnalysisPagedResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisSearchRequest;
import com.metanet.myddareungi.domain.analysis.model.AnalysisResult;
import com.metanet.myddareungi.domain.analysis.repository.AnalysisRepository;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @Mock
    private AnalysisRepository analysisRepository;

    @InjectMocks
    private AnalysisService analysisService;

    // ─────────────────────────────────────────────────────────
    // searchAnalysis 테스트
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("searchAnalysis - 결과가 있을 때 AnalysisPagedResponse를 올바르게 반환한다")
    void searchAnalysis_returnsPagedResponse() {
        // given
        AnalysisSearchRequest request = new AnalysisSearchRequest();
        request.setPage(1);

        AnalysisResult result = new AnalysisResult();
        result.setStationName("뚝섬역");
        result.setDistrict("성동구");
        result.setMonth(4);
        result.setAgeGroup("20대");
        result.setTotalUseCnt(300);

        given(analysisRepository.searchAnalysis(any())).willReturn(List.of(result));
        given(analysisRepository.countSearchAnalysis(any())).willReturn(1);

        // when
        AnalysisPagedResponse response = analysisService.searchAnalysis(request);

        // then
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getStationName()).isEqualTo("뚝섬역");
        assertThat(response.getContent().get(0).getDistrict()).isEqualTo("성동구");
        assertThat(response.getContent().get(0).getMonth()).isEqualTo(4);
        assertThat(response.getContent().get(0).getAgeGroup()).isEqualTo("20대");
        assertThat(response.getContent().get(0).getTotalUseCnt()).isEqualTo(300);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getCurrentPage()).isEqualTo(1);
    }

    @Test
    @DisplayName("searchAnalysis - 결과가 없을 때 빈 content와 totalPages=0 반환")
    void searchAnalysis_emptyResult_returnsEmptyResponse() {
        // given
        AnalysisSearchRequest request = new AnalysisSearchRequest();
        request.setPage(1);

        given(analysisRepository.searchAnalysis(any())).willReturn(Collections.emptyList());
        given(analysisRepository.countSearchAnalysis(any())).willReturn(0);

        // when
        AnalysisPagedResponse response = analysisService.searchAnalysis(request);

        // then
        assertThat(response.getContent()).isEmpty();
        assertThat(response.getTotalElements()).isEqualTo(0);
        assertThat(response.getTotalPages()).isEqualTo(0);
    }

    @Test
    @DisplayName("searchAnalysis - page가 null이면 기본값 1로 처리된다")
    void searchAnalysis_nullPage_defaultsToOne() {
        // given
        AnalysisSearchRequest request = new AnalysisSearchRequest();
        request.setPage(null);

        given(analysisRepository.searchAnalysis(any())).willReturn(Collections.emptyList());
        given(analysisRepository.countSearchAnalysis(any())).willReturn(0);

        // when
        AnalysisPagedResponse response = analysisService.searchAnalysis(request);

        // then
        assertThat(response.getCurrentPage()).isEqualTo(1);
    }

    @Test
    @DisplayName("searchAnalysis - totalPages는 ceil(totalElements / pageSize)로 계산된다")
    void searchAnalysis_totalPagesCalculatedCorrectly() {
        // given
        AnalysisSearchRequest request = new AnalysisSearchRequest();
        request.setPage(1);

        given(analysisRepository.searchAnalysis(any())).willReturn(Collections.emptyList());
        given(analysisRepository.countSearchAnalysis(any())).willReturn(25); // ceil(25/12) = 3

        // when
        AnalysisPagedResponse response = analysisService.searchAnalysis(request);

        // then
        assertThat(response.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("searchAnalysis - offset은 (page-1) * 12로 설정된다")
    void searchAnalysis_offsetSetCorrectly() {
        // given
        AnalysisSearchRequest request = new AnalysisSearchRequest();
        request.setPage(3);

        given(analysisRepository.searchAnalysis(any())).willReturn(Collections.emptyList());
        given(analysisRepository.countSearchAnalysis(any())).willReturn(0);

        // when
        analysisService.searchAnalysis(request);

        // then: offset = (3-1)*12 = 24, pageSize = 12
        assertThat(request.getOffset()).isEqualTo(24);
        assertThat(request.getPageSize()).isEqualTo(12);
    }

    // ─────────────────────────────────────────────────────────
    // listAllAnalysis 테스트
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("listAllAnalysis - 복수의 결과를 AnalysisResponse 리스트로 변환한다")
    void listAllAnalysis_mapsResultsToResponse() {
        // given
        AnalysisSearchRequest request = new AnalysisSearchRequest();

        AnalysisResult r1 = new AnalysisResult();
        r1.setStationName("여의도역");
        r1.setDistrict("영등포구");
        r1.setMonth(5);
        r1.setAgeGroup("30대");
        r1.setTotalUseCnt(500);

        AnalysisResult r2 = new AnalysisResult();
        r2.setStationName("강남역");
        r2.setDistrict("강남구");
        r2.setMonth(6);
        r2.setAgeGroup("40대");
        r2.setTotalUseCnt(750);

        given(analysisRepository.listAllAnalysis(request)).willReturn(List.of(r1, r2));

        // when
        List<AnalysisResponse> result = analysisService.listAllAnalysis(request);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStationName()).isEqualTo("여의도역");
        assertThat(result.get(1).getStationName()).isEqualTo("강남역");
        assertThat(result.get(1).getTotalUseCnt()).isEqualTo(750);
    }

    @Test
    @DisplayName("listAllAnalysis - 결과가 없으면 빈 리스트를 반환한다")
    void listAllAnalysis_emptyResult_returnsEmptyList() {
        // given
        AnalysisSearchRequest request = new AnalysisSearchRequest();
        given(analysisRepository.listAllAnalysis(request)).willReturn(Collections.emptyList());

        // when
        List<AnalysisResponse> result = analysisService.listAllAnalysis(request);

        // then
        assertThat(result).isEmpty();
    }
}
