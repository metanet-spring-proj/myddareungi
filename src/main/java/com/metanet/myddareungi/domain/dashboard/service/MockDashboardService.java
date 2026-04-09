//package com.metanet.myddareungi.domain.dashboard.service;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Service;
//
//import com.metanet.myddareungi.domain.dashboard.dto.BikeAgeGroupSummaryResponseDto;
//import com.metanet.myddareungi.domain.dashboard.dto.BikeDistrictSummaryResponseDto;
//import com.metanet.myddareungi.domain.dashboard.dto.BikeKpiSummaryResponseDto;
//import com.metanet.myddareungi.domain.dashboard.dto.BikeMonthlySummaryResponseDto;
//import com.metanet.myddareungi.domain.dashboard.dto.BikeRentTypeSummaryResponseDto;
//import com.metanet.myddareungi.domain.dashboard.dto.BikeWeekdaySummaryResponseDto;
//
//@Service
//@Profile("local")
//public class MockDashboardService implements IDashboardService {
//
//	@Override
//	public BikeKpiSummaryResponseDto getKpi() {
//		BikeKpiSummaryResponseDto dto = new BikeKpiSummaryResponseDto();
//		dto.setTotalUseCount(1256843L);
//		dto.setTotalCarbonSaved(new BigDecimal("28453.72"));
//		dto.setTotalStationCount(278L);
//		dto.setAvgUseTime(new BigDecimal("18.45"));
//		dto.setTopDistrict("강남구");
//		return dto;
//	}
//
//	@Override
//	public BikeMonthlySummaryResponseDto getMonthlySummary() {
//		BikeMonthlySummaryResponseDto dto = new BikeMonthlySummaryResponseDto();
//		dto.setMonthList(Arrays.asList("202401", "202402", "202403", "202404", "202405", "202406"));
//		dto.setUseCountList(Arrays.asList(12000L, 15000L, 18000L, 21000L, 25000L, 23000L));
//		dto.setStationCountList(Arrays.asList(210L, 215L, 220L, 225L, 230L, 235L));
//		return dto;
//	}
//
//	@Override
//	public BikeWeekdaySummaryResponseDto getWeekDaySummary() {
//		BikeWeekdaySummaryResponseDto dto = new BikeWeekdaySummaryResponseDto();
//		dto.setWeekdayList(Arrays.asList("월", "화", "수", "목", "금", "토", "일"));
//		dto.setUseCountList(Arrays.asList(3200L, 3400L, 3600L, 3550L, 4100L, 5200L, 4700L));
//		return dto;
//	}
//
//	@Override
//	public BikeAgeGroupSummaryResponseDto getAgeGroupSummary() {
//		BikeAgeGroupSummaryResponseDto dto = new BikeAgeGroupSummaryResponseDto();
//		dto.setAgeGroupList(Arrays.asList("~10대", "20대", "30대", "40대", "50대", "60대", "70대~"));
//		dto.setUseCountList(Arrays.asList(1800L, 8200L, 7600L, 5400L, 3900L, 2100L, 700L));
//		return dto;
//	}
//
//	@Override
//	public BikeDistrictSummaryResponseDto getDistrictSummary() {
//		BikeDistrictSummaryResponseDto dto = new BikeDistrictSummaryResponseDto();
//		dto.setDistrictList(Arrays.asList("강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구",
//				"동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"));
//
//		dto.setUseCountList(Arrays.asList(9800L, // 강남구
//				6200L, // 강동구
//				4100L, // 강북구
//				7600L, // 강서구
//				6900L, // 관악구
//				7200L, // 광진구
//				5800L, // 구로구
//				4300L, // 금천구
//				6400L, // 노원구
//				3900L, // 도봉구
//				5300L, // 동대문구
//				6100L, // 동작구
//				8700L, // 마포구
//				5200L, // 서대문구
//				7900L, // 서초구
//				6800L, // 성동구
//				5000L, // 성북구
//				9100L, // 송파구
//				5600L, // 양천구
//				8300L, // 영등포구
//				4700L, // 용산구
//				4900L, // 은평구
//				7100L, // 종로구
//				5400L, // 중구
//				4600L // 중랑구
//		));
//		return dto;
//	}
//
//	@Override
//	public BikeRentTypeSummaryResponseDto getRentTypeSummary() {
//		BikeRentTypeSummaryResponseDto dto = new BikeRentTypeSummaryResponseDto();
//		dto.setRentTypeList(Arrays.asList("정기권", "일일권", "단체권", "기타"));
//		dto.setUseCountList(Arrays.asList(18500L, 9200L, 2100L, 800L));
//		return dto;
//	}
//}