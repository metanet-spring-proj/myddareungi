package com.metanet.myddareungi.domain.dashboard.controller;

import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.SecurityConfig;
import com.metanet.myddareungi.config.JwtTokenProvider;
import com.metanet.myddareungi.domain.dashboard.dto.*;
import com.metanet.myddareungi.domain.dashboard.service.IDashboardService;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardApiController.class)
@Import(SecurityConfig.class)
class DashboardApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    IDashboardService dashboardService;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    JwtCookieUtils jwtCookieUtils;

    @MockitoBean
    MemberAuthService memberAuthService;

    // ── GET /api/v1/dashboard/kpi ─────────────
    @Test
    @WithMockUser
    @DisplayName("GET /kpi - 로그인 사용자 → 200 OK")
    void getKpi_로그인_200() throws Exception {
        // given
        BikeKpiSummaryResponseDto dto = new BikeKpiSummaryResponseDto();
        dto.setTotalUseCnt(123456L);
        dto.setTopDistrict("강남구");
        when(dashboardService.getKpi()).thenReturn(dto);

        // when & then
        mockMvc.perform(get("/api/v1/dashboard/kpi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUseCnt").value(123456L))
                .andExpect(jsonPath("$.topDistrict").value("강남구"));
    }

    @Test
    @DisplayName("GET /kpi - 비로그인 → 리디렉션")
    void getKpi_비로그인_리디렉션() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/kpi"))
                .andExpect(status().is3xxRedirection());
    }

    // ── GET /api/v1/dashboard/monthly-summary ─
    @Test
    @WithMockUser
    @DisplayName("GET /monthly-summary - 로그인 사용자 → 200 OK")
    void getMonthlySummary_로그인_200() throws Exception {
        // given
        BikeMonthlySummaryResponseDto dto = new BikeMonthlySummaryResponseDto();
        dto.setMonthList(List.of("2024-01", "2024-02"));
        dto.setUseCountList(List.of(5000L, 6000L));
        dto.setStationCountList(List.of(200L, 210L));
        when(dashboardService.getMonthlySummary()).thenReturn(dto);

        // when & then
        mockMvc.perform(get("/api/v1/dashboard/monthly-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthList[0]").value("2024-01"))
                .andExpect(jsonPath("$.useCountList[0]").value(5000));
    }

    // ── GET /api/v1/dashboard/weekday-summary ─
    @Test
    @WithMockUser
    @DisplayName("GET /weekday-summary - 로그인 사용자 → 200 OK")
    void getWeekdaySummary_로그인_200() throws Exception {
        // given
        BikeWeekdaySummaryResponseDto dto = new BikeWeekdaySummaryResponseDto();
        dto.setWeekdayList(List.of("월", "화"));
        dto.setUseCountList(List.of(1000L, 1200L));
        when(dashboardService.getWeekDaySummary()).thenReturn(dto);

        // when & then
        mockMvc.perform(get("/api/v1/dashboard/weekday-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weekdayList[0]").value("월"));
    }

    // ── GET /api/v1/dashboard/age-group-summary
    @Test
    @WithMockUser
    @DisplayName("GET /age-group-summary - 로그인 사용자 → 200 OK")
    void getAgeGroupSummary_로그인_200() throws Exception {
        // given
        BikeAgeGroupSummaryResponseDto dto = new BikeAgeGroupSummaryResponseDto();
        dto.setAgeGroupList(List.of("20대", "30대"));
        dto.setUseCountList(List.of(8000L, 9500L));
        when(dashboardService.getAgeGroupSummary()).thenReturn(dto);

        // when & then
        mockMvc.perform(get("/api/v1/dashboard/age-group-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ageGroupList[0]").value("20대"));
    }

    // ── GET /api/v1/dashboard/district-summary ─
    @Test
    @WithMockUser
    @DisplayName("GET /district-summary - 로그인 사용자 → 200 OK")
    void getDistrictSummary_로그인_200() throws Exception {
        // given
        BikeDistrictSummaryResponseDto dto = new BikeDistrictSummaryResponseDto();
        dto.setDistrictList(List.of("강남구", "마포구"));
        dto.setUseCountList(List.of(15000L, 12000L));
        when(dashboardService.getDistrictSummary()).thenReturn(dto);

        // when & then
        mockMvc.perform(get("/api/v1/dashboard/district-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.districtList[0]").value("강남구"));
    }

    // ── GET /api/v1/dashboard/rent-type-summary
    @Test
    @WithMockUser
    @DisplayName("GET /rent-type-summary - 로그인 사용자 → 200 OK")
    void getRentTypeSummary_로그인_200() throws Exception {
        // given
        BikeRentTypeSummaryResponseDto dto = new BikeRentTypeSummaryResponseDto();
        dto.setRentTypeList(List.of("1시간권", "정기권"));
        dto.setUseCountList(List.of(7000L, 11000L));
        when(dashboardService.getRentTypeSummary()).thenReturn(dto);

        // when & then
        mockMvc.perform(get("/api/v1/dashboard/rent-type-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentTypeList[0]").value("1시간권"));
    }
}