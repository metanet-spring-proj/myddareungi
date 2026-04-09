package com.metanet.myddareungi.domain.dashboard.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BikeDistrictSummary {
    private Long id;
    private String month;
    private String district;
    private Long totalUseCnt;
    private LocalDateTime createdAt;
}