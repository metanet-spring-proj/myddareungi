package com.metanet.myddareungi.domain.dashboard.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metanet.myddareungi.domain.dashboard.model.BikeAgeGroupSummary;
import com.metanet.myddareungi.domain.dashboard.model.BikeDistrictSummary;
import com.metanet.myddareungi.domain.dashboard.model.BikeKpi;
import com.metanet.myddareungi.domain.dashboard.model.BikeMonthlySummary;
import com.metanet.myddareungi.domain.dashboard.model.BikeRentTypeSummary;
import com.metanet.myddareungi.domain.dashboard.model.BikeWeekdaySummary;

@Mapper
@Repository
public interface DashboardRepository {

    BikeKpi selectLatestKpi();

    List<BikeMonthlySummary> selectMonthlySummaryList();

    List<BikeWeekdaySummary> selectWeekdaySummaryList();

    List<BikeAgeGroupSummary> selectAgeGroupSummaryList();

    List<BikeDistrictSummary> selectDistrictSummaryList();
    
    List<BikeRentTypeSummary> selectRentTypeSummaryList();
}