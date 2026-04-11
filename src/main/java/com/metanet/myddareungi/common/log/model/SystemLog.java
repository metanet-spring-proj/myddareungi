package com.metanet.myddareungi.common.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLog {

    private Long logId;
    private Long userId;
    private String methodName;
    private String arguments;
    private String errorMessage;
    private Long executionTime;
    private LocalDateTime createdAt;
}