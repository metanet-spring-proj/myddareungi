package com.metanet.myddareungi.domain.analysis.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.JwtTokenProvider;
import com.metanet.myddareungi.config.SecurityConfig;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisResponse;
import com.metanet.myddareungi.domain.analysis.service.IAnalysisService;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;

@WebMvcTest(AnalysisApiController.class)
@Import(SecurityConfig.class)
class AnalysisApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IAnalysisService analysisService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtCookieUtils jwtCookieUtils;

    @MockitoBean
    private MemberAuthService memberAuthService;

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    @DisplayName("인증된 사용자가 CSV 내보내기 요청 시 200 OK와 Content-Disposition 헤더가 반환된다")
    void exportCsv_authenticated_returnsOkWithAttachmentHeader() throws Exception {
        // given
        given(analysisService.listAllAnalysis(any())).willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/v1/analysis/export/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", org.hamcrest.Matchers.containsString("text/csv")))
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("attachment")));
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    @DisplayName("CSV 내보내기 - 데이터가 있을 때 각 행이 올바르게 출력된다")
    void exportCsv_withData_writesRowsCorrectly() throws Exception {
        // given
        List<AnalysisResponse> data = List.of(
                AnalysisResponse.builder()
                        .stationName("여의도역")
                        .district("영등포구")
                        .month(5)
                        .ageGroup("30대")
                        .totalUseCnt(500)
                        .build()
        );
        given(analysisService.listAllAnalysis(any())).willReturn(data);

        // when & then
        mockMvc.perform(get("/api/v1/analysis/export/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String body = result.getResponse().getContentAsString();
                    // BOM 이후 헤더 행 확인
                    assert body.contains("대여소 명");
                    assert body.contains("여의도역");
                    assert body.contains("영등포구");
                });
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    @DisplayName("CSV 내보내기 - 데이터가 없을 때 헤더 행만 포함된 CSV가 반환된다")
    void exportCsv_noData_returnsHeaderOnly() throws Exception {
        // given
        given(analysisService.listAllAnalysis(any())).willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/v1/analysis/export/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String body = result.getResponse().getContentAsString();
                    assert body.contains("대여소 명");
                    // 데이터 행은 없어야 함
                    assert !body.contains("여의도역");
                });
    }

    @Test
    @DisplayName("인증되지 않은 사용자가 CSV 내보내기 요청 시 리다이렉트된다")
    void exportCsv_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/api/v1/analysis/export/csv"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    @DisplayName("CSV 내보내기 - 필터 파라미터가 전달될 때 서비스가 호출된다")
    void exportCsv_withFilter_callsServiceWithRequest() throws Exception {
        // given
        given(analysisService.listAllAnalysis(any())).willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/v1/analysis/export/csv")
                        .param("month", "4")
                        .param("district", "강남구"))
                .andExpect(status().isOk());
    }
}
