package com.metanet.myddareungi.domain.dashboard.service;

import com.metanet.myddareungi.domain.dashboard.dto.*;
import com.metanet.myddareungi.domain.dashboard.model.*;
import com.metanet.myddareungi.domain.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    DashboardRepository dashboardRepository;

    @InjectMocks
    DashboardService dashboardService;

    // ── getKpi() ──────────────────────────────
    @Test
    @DisplayName("getKpi() - Repository 값이 DTO에 올바르게 매핑된다")
    void getKpi_필드매핑_정상() {
        // given
        BikeKpi fakeKpi = new BikeKpi();
        fakeKpi.setTotalUseCnt(123456L);
        fakeKpi.setTotalCarbonSaved(new BigDecimal("999.99"));
        fakeKpi.setTotalStationCnt(300L);
        fakeKpi.setAvgUseTime(new BigDecimal("25.5"));
        fakeKpi.setTopDistrict("강남구");

        when(dashboardRepository.selectLatestKpi()).thenReturn(fakeKpi);

        // when
        BikeKpiSummaryResponseDto result = dashboardService.getKpi();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalUseCnt()).isEqualTo(123456L);
        assertThat(result.getTotalCarbonSaved()).isEqualByComparingTo(new BigDecimal("999.99"));
        assertThat(result.getTotalStationCnt()).isEqualTo(300L);
        assertThat(result.getAvgUseTime()).isEqualByComparingTo(new BigDecimal("25.5"));
        assertThat(result.getTopDistrict()).isEqualTo("강남구");
    }

    // ── getMonthlySummary() ───────────────────
    @Test
    @DisplayName("getMonthlySummary() - month, useCount, stationCount 리스트가 올바르게 변환된다")
    void getMonthlySummary_리스트변환_정상() {
        // given
        BikeMonthlySummary item1 = new BikeMonthlySummary();
        item1.setMonth("2024-01");
        item1.setTotalUseCnt(5000L);
        item1.setTotalStationCnt(new BigDecimal("200"));

        BikeMonthlySummary item2 = new BikeMonthlySummary();
        item2.setMonth("2024-02");
        item2.setTotalUseCnt(6000L);
        item2.setTotalStationCnt(new BigDecimal("210"));

        when(dashboardRepository.selectMonthlySummaryList()).thenReturn(List.of(item1, item2));

        // when
        BikeMonthlySummaryResponseDto result = dashboardService.getMonthlySummary();

        // then
        assertThat(result.getMonthList()).containsExactly("2024-01", "2024-02");
        assertThat(result.getUseCountList()).containsExactly(5000L, 6000L);
        assertThat(result.getStationCountList()).containsExactly(200L, 210L);
    }

    @Test
    @DisplayName("getMonthlySummary() - stationCount가 null이면 0L로 처리된다")
    void getMonthlySummary_stationCount_null이면_0처리() {
        // given
        BikeMonthlySummary item = new BikeMonthlySummary();
        item.setMonth("2024-03");
        item.setTotalUseCnt(3000L);
        item.setTotalStationCnt(null);

        when(dashboardRepository.selectMonthlySummaryList()).thenReturn(List.of(item));

        // when
        BikeMonthlySummaryResponseDto result = dashboardService.getMonthlySummary();

        // then
        assertThat(result.getStationCountList()).containsExactly(0L);
    }

    // ── getWeekDaySummary() ───────────────────
    @Test
    @DisplayName("getWeekDaySummary() - weekday, useCount 리스트가 올바르게 변환된다")
    void getWeekDaySummary_리스트변환_정상() {
        // given
        BikeWeekdaySummary mon = new BikeWeekdaySummary();
        mon.setWeekday("월");
        mon.setTotalUseCnt(1000L);

        BikeWeekdaySummary tue = new BikeWeekdaySummary();
        tue.setWeekday("화");
        tue.setTotalUseCnt(1200L);

        when(dashboardRepository.selectWeekdaySummaryList()).thenReturn(List.of(mon, tue));

        // when
        BikeWeekdaySummaryResponseDto result = dashboardService.getWeekDaySummary();

        // then
        assertThat(result.getWeekdayList()).containsExactly("월", "화");
        assertThat(result.getUseCountList()).containsExactly(1000L, 1200L);
    }

    // ── getAgeGroupSummary() ──────────────────
    @Test
    @DisplayName("getAgeGroupSummary() - ageGroup, useCount 리스트가 올바르게 변환된다")
    void getAgeGroupSummary_리스트변환_정상() {
        // given
        BikeAgeGroupSummary age20 = new BikeAgeGroupSummary();
        age20.setAgeGroup("20대");
        age20.setTotalUseCnt(8000L);

        BikeAgeGroupSummary age30 = new BikeAgeGroupSummary();
        age30.setAgeGroup("30대");
        age30.setTotalUseCnt(9500L);

        when(dashboardRepository.selectAgeGroupSummaryList()).thenReturn(List.of(age20, age30));

        // when
        BikeAgeGroupSummaryResponseDto result = dashboardService.getAgeGroupSummary();

        // then
        assertThat(result.getAgeGroupList()).containsExactly("20대", "30대");
        assertThat(result.getUseCountList()).containsExactly(8000L, 9500L);
    }

    // ── getDistrictSummary() ──────────────────
    @Test
    @DisplayName("getDistrictSummary() - district, useCount 리스트가 올바르게 변환된다")
    void getDistrictSummary_리스트변환_정상() {
        // given
        BikeDistrictSummary gangnam = new BikeDistrictSummary();
        gangnam.setDistrict("강남구");
        gangnam.setTotalUseCnt(15000L);

        BikeDistrictSummary mapo = new BikeDistrictSummary();
        mapo.setDistrict("마포구");
        mapo.setTotalUseCnt(12000L);

        when(dashboardRepository.selectDistrictSummaryList()).thenReturn(List.of(gangnam, mapo));

        // when
        BikeDistrictSummaryResponseDto result = dashboardService.getDistrictSummary();

        // then
        assertThat(result.getDistrictList()).containsExactly("강남구", "마포구");
        assertThat(result.getUseCountList()).containsExactly(15000L, 12000L);
    }

    // ── getRentTypeSummary() ──────────────────
    @Test
    @DisplayName("getRentTypeSummary() - rentType, useCount 리스트가 올바르게 변환된다")
    void getRentTypeSummary_리스트변환_정상() {
        // given
        BikeRentTypeSummary type1 = new BikeRentTypeSummary();
        type1.setRentType("1시간권");
        type1.setTotalUseCnt(7000L);

        BikeRentTypeSummary type2 = new BikeRentTypeSummary();
        type2.setRentType("정기권");
        type2.setTotalUseCnt(11000L);

        when(dashboardRepository.selectRentTypeSummaryList()).thenReturn(List.of(type1, type2));

        // when
        BikeRentTypeSummaryResponseDto result = dashboardService.getRentTypeSummary();

        // then
        assertThat(result.getRentTypeList()).containsExactly("1시간권", "정기권");
        assertThat(result.getUseCountList()).containsExactly(7000L, 11000L);
    }
}