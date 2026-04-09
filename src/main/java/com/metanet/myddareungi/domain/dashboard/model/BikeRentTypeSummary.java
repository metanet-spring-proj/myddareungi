package com.metanet.myddareungi.domain.dashboard.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BikeRentTypeSummary {
    private Long id;
    private String month;
    private String rentType;
    private Long totalUseCnt;
    private LocalDateTime createdAt;
}