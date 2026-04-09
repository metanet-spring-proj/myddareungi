package com.metanet.myddareungi.domain.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class BikeKpiSummaryResponseDto {
	  private Long totalUseCount;
	    private BigDecimal totalCarbonSaved;
	    private Long totalStationCount;
	    private BigDecimal avgUseTime;
	    private String topDistrict;
}
