package com.metanet.myddareungi.domain.dashboard.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BikeMonthlySummary {
    private Long id;
    private String month;
    private Long totalUseCnt;
    private BigDecimal totalStationCnt;
    private LocalDateTime createdAt;
}