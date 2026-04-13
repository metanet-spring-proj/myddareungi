package com.metanet.myddareungi.domain.analysis.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import com.metanet.myddareungi.domain.analysis.dto.AnalysisPagedResponse;
import com.metanet.myddareungi.domain.analysis.dto.AnalysisResponse;
import com.metanet.myddareungi.domain.analysis.service.IAnalysisService;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;

@WebMvcTest(AnalysisController.class)
@Import(SecurityConfig.class)
class AnalysisControllerTest {

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
    @DisplayName("인증된 사용자가 /analysis/search 접근 시 200 OK와 analysis/analysis 뷰를 반환한다")
    void searchAnalysis_authenticated_returnsOkAndView() throws Exception {
        // given
        AnalysisPagedResponse pagedResponse = AnalysisPagedResponse.builder()
                .content(Collections.emptyList())
                .totalElements(0)
                .totalPages(0)
                .currentPage(1)
                .build();

        given(analysisService.searchAnalysis(any())).willReturn(pagedResponse);

        // when & then
        mockMvc.perform(get("/analysis/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("analysis/analysis"));
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    @DisplayName("searchAnalysis - 서비스 결과가 모델에 올바르게 담긴다")
    void searchAnalysis_modelAttributesSet() throws Exception {
        // given
        List<AnalysisResponse> content = List.of(
                AnalysisResponse.builder()
                        .stationName("뚝섬역")
                        .district("성동구")
                        .month(4)
                        .ageGroup("20대")
                        .totalUseCnt(300)
                        .build()
        );

        AnalysisPagedResponse pagedResponse = AnalysisPagedResponse.builder()
                .content(content)
                .totalElements(1)
                .totalPages(1)
                .currentPage(1)
                .build();

        given(analysisService.searchAnalysis(any())).willReturn(pagedResponse);

        // when & then
        mockMvc.perform(get("/analysis/search")
                        .param("month", "4")
                        .param("page", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalElements", 1L))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("activeMenu", "analysis"));
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    @DisplayName("searchAnalysis - page 파라미터가 0 이하이면 1로 보정된다")
    void searchAnalysis_pageZeroOrNegative_resetToOne() throws Exception {
        // given
        AnalysisPagedResponse pagedResponse = AnalysisPagedResponse.builder()
                .content(Collections.emptyList())
                .totalElements(0)
                .totalPages(0)
                .currentPage(1)
                .build();

        given(analysisService.searchAnalysis(any())).willReturn(pagedResponse);

        // when & then — page=0 은 컨트롤러 내에서 1로 보정됨
        mockMvc.perform(get("/analysis/search").param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("analysis/analysis"));
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    @DisplayName("searchAnalysis - principal이 존재하면 username 모델 값이 설정된다")
    void searchAnalysis_withPrincipal_setsUsername() throws Exception {
        // given
        AnalysisPagedResponse pagedResponse = AnalysisPagedResponse.builder()
                .content(Collections.emptyList())
                .totalElements(0)
                .totalPages(0)
                .currentPage(1)
                .build();

        given(analysisService.searchAnalysis(any())).willReturn(pagedResponse);

        // when & then
        mockMvc.perform(get("/analysis/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("username", "user1"));
    }
}
