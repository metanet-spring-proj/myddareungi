package com.metanet.myddareungi.domain.dashboard.service;

import com.metanet.myddareungi.domain.dashboard.dto.BikeAgeGroupSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeDistrictSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeKpiSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeMonthlySummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeRentTypeSummaryResponseDto;
import com.metanet.myddareungi.domain.dashboard.dto.BikeWeekdaySummaryResponseDto;

public interface IDashboardService {

    BikeKpiSummaryResponseDto getKpi();

    BikeMonthlySummaryResponseDto getMonthlySummary();

    BikeWeekdaySummaryResponseDto getWeekDaySummary();

    BikeAgeGroupSummaryResponseDto getAgeGroupSummary();

    BikeDistrictSummaryResponseDto getDistrictSummary();
    
    BikeRentTypeSummaryResponseDto getRentTypeSummary();
}