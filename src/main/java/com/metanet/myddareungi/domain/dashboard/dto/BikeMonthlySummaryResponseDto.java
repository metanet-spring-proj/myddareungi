package com.metanet.myddareungi.domain.dashboard.dto;

import java.util.List;

import lombok.Data;

@Data
public class BikeMonthlySummaryResponseDto {
	    private List<String> monthList;
	    private List<Long> useCountList;
	    private List<Long> stationCountList;
	
}
