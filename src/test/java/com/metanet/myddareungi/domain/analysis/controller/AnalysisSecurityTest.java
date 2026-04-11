package com.metanet.myddareungi.domain.analysis.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.JwtTokenProvider;
import com.metanet.myddareungi.config.SecurityConfig;
import com.metanet.myddareungi.domain.analysis.service.IAnalysisService;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;

@WebMvcTest(AnalysisController.class)
@Import(SecurityConfig.class)
public class AnalysisSecurityTest {

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
    @DisplayName("인증되지 않은 사용자가 분석 페이지 접근 시 로그인 페이지로 리다이렉트된다")
    void analysisAccessUnauthenticatedRedirectToLogin() throws Exception {
        mockMvc.perform(get("/analysis/search"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
    
    @Test
    @DisplayName("인증되지 않은 사용자가 분석 API 접근 시 401 또는 로그인 페이지로 리다이렉트된다")
    void analysisApiAccessUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/analysis/export/csv"))
                .andExpect(status().is3xxRedirection());
    }
}
