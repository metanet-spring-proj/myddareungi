package com.metanet.myddareungi.domain.admin.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboardDto {
    private Long adminId;
    private String adminName;
    private String adminEmail;

    private int pendingCount;
    private int todayUploadCount;

    private List<PendingFileDto> pendingFiles;

    private int totalPages;
    private int currentPage;
}
