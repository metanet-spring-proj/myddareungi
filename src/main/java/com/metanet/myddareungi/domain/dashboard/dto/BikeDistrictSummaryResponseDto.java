package com.metanet.myddareungi.domain.dashboard.dto;

import java.util.List;

import lombok.Data;

@Data
public class BikeDistrictSummaryResponseDto {
	private List<String> districtList;
	private List<Long> useCountList;
}
