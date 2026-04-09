package com.metanet.myddareungi.domain.dashboard.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.metanet.myddareungi.domain.dashboard.dto.BikeAgeGroupSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeDistrictSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeKpiSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeMonthlySummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeRentTypeSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeWeekdaySummaryResponseDto;

@Service
@Profile("local")
public class MockDashboardService implements IDashboardService {

    @Override
    public BikeKpiSummaryResponseDto getKpi() {
        BikeKpiSummaryResponseDto dto = new BikeKpiSummaryResponseDto();
        dto.setTotalUseCount(1256843L);
        dto.setTotalCarbonSaved(new BigDecimal("28453.72"));
        dto.setTotalStationCount(278L);
        dto.setAvgUseTime(new BigDecimal("18.45"));
        dto.setTopDistrict("강남구");
        return dto;
    }

    @Override
    public BikeMonthlySummaryResponseDto getMonthlySummary() {
        BikeMonthlySummaryResponseDto dto = new BikeMonthlySummaryResponseDto();
        dto.setMonthList(Arrays.asList("202401", "202402", "202403", "202404", "202405", "202406"));
        dto.setUseCountList(Arrays.asList(12000L, 15000L, 18000L, 21000L, 25000L, 23000L));
        dto.setStationCountList(Arrays.asList(210L, 215L, 220L, 225L, 230L, 235L));
        return dto;
    }

    @Override
    public BikeWeekdaySummaryResponseDto getWeekDaySummary() {
        BikeWeekdaySummaryResponseDto dto = new BikeWeekdaySummaryResponseDto();
        dto.setWeekdayList(Arrays.asList("월", "화", "수", "목", "금", "토", "일"));
        dto.setUseCountList(Arrays.asList(3200L, 3400L, 3600L, 3550L, 4100L, 5200L, 4700L));
        return dto;
    }

    @Override
    public BikeAgeGroupSummaryResponseDto getAgeGroupSummary() {
        BikeAgeGroupSummaryResponseDto dto = new BikeAgeGroupSummaryResponseDto();
        dto.setAgeGroupList(Arrays.asList("~10대", "20대", "30대", "40대", "50대", "60대", "70대~"));
        dto.setUseCountList(Arrays.asList(1800L, 8200L, 7600L, 5400L, 3900L, 2100L, 700L));
        return dto;
    }

    @Override
    public BikeDistrictSummaryResponseDto getDistrictSummary() {
        BikeDistrictSummaryResponseDto dto = new BikeDistrictSummaryResponseDto();
        dto.setDistrictList(Arrays.asList(
                "강남구", "송파구", "마포구", "영등포구", "서초구",
                "종로구", "성동구", "강서구", "노원구", "관악구"
        ));
        dto.setUseCountList(Arrays.asList(
                9800L, 9100L, 8700L, 8300L, 7900L,
                7200L, 6900L, 6500L, 6100L, 5800L
        ));
        return dto;
    }
    
    @Override
    public BikeRentTypeSummaryResponseDto getRentTypeSummary() {
        BikeRentTypeSummaryResponseDto dto = new BikeRentTypeSummaryResponseDto();
        dto.setRentTypeList(Arrays.asList("정기권", "일일권", "단체권", "기타"));
        dto.setUseCountList(Arrays.asList(18500L, 9200L, 2100L, 800L));
        return dto;
    }
}