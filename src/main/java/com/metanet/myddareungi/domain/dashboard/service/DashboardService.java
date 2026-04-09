package com.metanet.myddareungi.domain.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.metanet.myddareungi.domain.dashboard.dto.BikeAgeGroupSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeDistrictSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeKpiSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeMonthlySummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeRentTypeSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeWeekdaySummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.model.BikeAgeGroupSummary;
import com.metanet.myddareungi.domain.dashboard.model.BikeDistrictSummary;
import com.metanet.myddareungi.domain.dashboard.model.BikeKpi;
import com.metanet.myddareungi.domain.dashboard.model.BikeMonthlySummary;
import com.metanet.myddareungi.domain.dashboard.model.BikeRentTypeSummary;
import com.metanet.myddareungi.domain.dashboard.model.BikeWeekdaySummary;
import com.metanet.myddareungi.domain.dashboard.repository.DashboardRepository;

import lombok.RequiredArgsConstructor;

@Service
//@Profile("dev")
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public BikeKpiSummaryResponseDto getKpi() {
        BikeKpi bikeKpi = dashboardRepository.selectLatestKpi();

        BikeKpiSummaryResponseDto responseDto = new BikeKpiSummaryResponseDto();
        responseDto.setTotalUseCnt(bikeKpi.getTotalUseCnt());
        responseDto.setTotalCarbonSaved(bikeKpi.getTotalCarbonSaved());
        responseDto.setTotalStationCnt(bikeKpi.getTotalStationCnt());
        responseDto.setAvgUseTime(bikeKpi.getAvgUseTime());
        responseDto.setTopDistrict(bikeKpi.getTopDistrict());
System.out.println();
        return responseDto;
    }

    @Override
    public BikeMonthlySummaryResponseDto getMonthlySummary() {
        List<BikeMonthlySummary> monthlySummaryList = dashboardRepository.selectMonthlySummaryList();

        List<String> monthList = new ArrayList<>();
        List<Long> useCountList = new ArrayList<>();
        List<Long> stationCountList = new ArrayList<>();

        for (BikeMonthlySummary item : monthlySummaryList) {
            monthList.add(item.getMonth());
            useCountList.add(item.getTotalUseCnt());
            stationCountList.add(item.getTotalStationCnt() == null ? 0L : item.getTotalStationCnt().longValue());
        }

        BikeMonthlySummaryResponseDto responseDto = new BikeMonthlySummaryResponseDto();
        responseDto.setMonthList(monthList);
        responseDto.setUseCountList(useCountList);
        responseDto.setStationCountList(stationCountList);

        return responseDto;
    }

    @Override
    public BikeWeekdaySummaryResponseDto getWeekDaySummary() {
        List<BikeWeekdaySummary> weekdaySummaryList = dashboardRepository.selectWeekdaySummaryList();

        List<String> weekdayList = new ArrayList<>();
        List<Long> useCountList = new ArrayList<>();

        for (BikeWeekdaySummary item : weekdaySummaryList) {
            weekdayList.add(item.getWeekday());
            useCountList.add(item.getTotalUseCnt());
        }

        BikeWeekdaySummaryResponseDto responseDto = new BikeWeekdaySummaryResponseDto();
        responseDto.setWeekdayList(weekdayList);
        responseDto.setUseCountList(useCountList);

        return responseDto;
    }

    @Override
    public BikeAgeGroupSummaryResponseDto getAgeGroupSummary() {
        List<BikeAgeGroupSummary> ageGroupSummaryList = dashboardRepository.selectAgeGroupSummaryList();

        List<String> ageGroupList = new ArrayList<>();
        List<Long> useCountList = new ArrayList<>();

        for (BikeAgeGroupSummary item : ageGroupSummaryList) {
            ageGroupList.add(item.getAgeGroup());
            useCountList.add(item.getTotalUseCnt());
        }

        BikeAgeGroupSummaryResponseDto responseDto = new BikeAgeGroupSummaryResponseDto();
        responseDto.setAgeGroupList(ageGroupList);
        responseDto.setUseCountList(useCountList);

        return responseDto;
    }

    @Override
    public BikeDistrictSummaryResponseDto getDistrictSummary() {
        List<BikeDistrictSummary> districtSummaryList = dashboardRepository.selectDistrictSummaryList();

        List<String> districtList = new ArrayList<>();
        List<Long> useCountList = new ArrayList<>();

        for (BikeDistrictSummary item : districtSummaryList) {
            districtList.add(item.getDistrict());
            useCountList.add(item.getTotalUseCnt());
        }

        BikeDistrictSummaryResponseDto responseDto = new BikeDistrictSummaryResponseDto();
        responseDto.setDistrictList(districtList);
        responseDto.setUseCountList(useCountList);

        return responseDto;
    }
    
    @Override
    public BikeRentTypeSummaryResponseDto getRentTypeSummary() {
        List<BikeRentTypeSummary> list = dashboardRepository.selectRentTypeSummaryList();

        List<String> rentTypeList = new ArrayList<>();
        List<Long> useCountList = new ArrayList<>();

        for (BikeRentTypeSummary item : list) {
            rentTypeList.add(item.getRentType());
            useCountList.add(item.getTotalUseCnt());
        }

        BikeRentTypeSummaryResponseDto dto = new BikeRentTypeSummaryResponseDto();
        dto.setRentTypeList(rentTypeList);
        dto.setUseCountList(useCountList);

        return dto;
    }
}