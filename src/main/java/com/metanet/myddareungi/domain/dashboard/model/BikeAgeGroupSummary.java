package com.metanet.myddareungi.domain.dashboard.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BikeAgeGroupSummary {
    private Long id;
    private String month;
    private String ageGroup;
    private Long totalUseCnt;
    private LocalDateTime createdAt;
}