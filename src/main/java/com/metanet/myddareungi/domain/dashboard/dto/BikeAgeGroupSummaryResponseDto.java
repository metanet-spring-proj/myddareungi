package com.metanet.myddareungi.domain.dashboard.dto;

import java.util.List;

import lombok.Data;

@Data
public class BikeAgeGroupSummaryResponseDto {
	private List<String> ageGroupList;
	private List<Long> useCountList;
}
