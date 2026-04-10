package com.metanet.myddareungi.domain.dashboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metanet.myddareungi.domain.dashboard.dto.BikeAgeGroupSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeDistrictSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeKpiSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeMonthlySummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeRentTypeSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeWeekdaySummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.service.IDashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard API", description = "대시보드 조회 API")
public class DashboardApiController {

    private final IDashboardService dashboardService;

    @Operation(summary = "KPI 조회", description = "대시보드 상단 KPI 정보를 조회한다.")
    @GetMapping("/kpi")
    public ResponseEntity<BikeKpiSummaryResponseDto> getKpi() {
        return ResponseEntity.ok(dashboardService.getKpi());
    }

    @Operation(summary = "월별 이용건수 조회", description = "월별 집계 결과(이용건수, 대여소 수)를 조회한다.")
    @GetMapping("/monthly-summary")
    public ResponseEntity<BikeMonthlySummaryResponseDto> getMonthlySummary(
         
    ) {
        return ResponseEntity.ok(dashboardService.getMonthlySummary());
    }
    
    @Operation(summary = "요일별 이용건수 조회", description = "요일별 집계 결과(이용건수)를 조회한다.")
    @GetMapping("/weekday-summary")
    public ResponseEntity<BikeWeekdaySummaryResponseDto> getWeekDaySummary(
         
    ) {
        return ResponseEntity.ok(dashboardService.getWeekDaySummary());
    }
    @Operation(summary = "연령대별 이용건수 조회", description = "연령대별 집계 결과(이용건수)를 조회한다.")
    @GetMapping("/age-group-summary")
    public ResponseEntity<BikeAgeGroupSummaryResponseDto> getAgeGroupSummary(
  
    ) {
        return ResponseEntity.ok(dashboardService.getAgeGroupSummary());
    }

    @Operation(summary = "자치구별 이용건수 조회", description = "자치구별 집계 결과(이용건수)를 조회한다.")
    @GetMapping("/district-summary")
    public ResponseEntity<BikeDistrictSummaryResponseDto> getDistrictSummary(
  
    ) {
        return ResponseEntity.ok(dashboardService.getDistrictSummary());
    }
    
    @Operation(summary = "이용권 종류별 이용건수 조회", description = "이용권 종류별 집계 결과(이용건수)를 조회한다.")
    @GetMapping("/rent-type-summary")
    public ResponseEntity<BikeRentTypeSummaryResponseDto> getRentTypeSummary() {
        return ResponseEntity.ok(dashboardService.getRentTypeSummary());
    }
}