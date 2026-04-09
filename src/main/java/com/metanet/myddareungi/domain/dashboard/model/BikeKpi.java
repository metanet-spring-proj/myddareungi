package com.metanet.myddareungi.domain.dashboard.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BikeKpi {
    private Long totalUseCnt;        
    private BigDecimal totalCarbonSaved;
    private Long totalStationCnt;   
    private BigDecimal avgUseTime;
    private String topDistrict;
    private LocalDateTime createdAt;
}