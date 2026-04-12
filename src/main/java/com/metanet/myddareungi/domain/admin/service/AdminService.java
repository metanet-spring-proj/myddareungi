package com.metanet.myddareungi.domain.admin.service;

import org.springframework.stereotype.Service;
import com.metanet.myddareungi.domain.admin.dto.AdminDashboardDto;
import com.metanet.myddareungi.domain.admin.repository.IAdminRepository;
import com.metanet.myddareungi.domain.member.model.Member;

import com.metanet.myddareungi.domain.files.service.IUploadFileService;
import com.metanet.myddareungi.domain.files.model.UploadFile;
import com.metanet.myddareungi.domain.admin.dto.PendingFileDto;
import java.util.List;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

@Service
public class AdminService implements IAdminService {

    private final IAdminRepository adminRepository;
    private final IUploadFileService uploadFileService;

    public AdminService(IAdminRepository adminRepository,
            IUploadFileService uploadFileService) {
        this.adminRepository = adminRepository;
        this.uploadFileService = uploadFileService;
    }

    @Override
    public AdminDashboardDto getDashboardData(long userId, int page, int size) {
        AdminDashboardDto dashboardDto = new AdminDashboardDto();

        // 1. 관리자 정보 조회
        Member admin = adminRepository.getAdminInfo(userId);
        if (admin != null) {
            dashboardDto.setAdminId(admin.getUserId());
            dashboardDto.setAdminName(admin.getUserName());
            dashboardDto.setAdminEmail(admin.getEmail());
        } else {
            // 기본값 설정 (정보가 없는 경우)
            dashboardDto.setAdminName("관리자");
            dashboardDto.setAdminEmail("admin@example.com");
        }

        // 2. 통계 데이터 조회 (검토 대기 수, 금일 업로드 수)
        int pendingCount = adminRepository.countPendingFiles();
        dashboardDto.setPendingCount(pendingCount);
        dashboardDto.setTodayUploadCount(adminRepository.countTodayUploads());

        // 3. 페이징 계산
        int totalPages = (pendingCount == 0) ? 1 : (int) Math.ceil((double) pendingCount / size);
        page = Math.max(0, Math.min(page, totalPages - 1));
        dashboardDto.setTotalPages(totalPages);
        dashboardDto.setCurrentPage(page);

        // 4. 파일 목록 페이징 조회 (PENDING만)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");

        List<PendingFileDto> mappedFiles = adminRepository.getPendingFilesPaged(page * size, size).stream()
                .map(file -> {
                    PendingFileDto dto = new PendingFileDto();
                    dto.setFileId(file.getFileId());
                    dto.setFileName(file.getFileName());
                    dto.setStatus(file.getStatus());
                    dto.setUploadDate(file.getUploadedAt() != null ? sdf.format(file.getUploadedAt()) : "-");

                    // 작성자 정보는 uploaderId 사용 (이름 조회 기능 제거)
                    dto.setWriter(String.valueOf(file.getUploaderId()));

                    // 파일 크기 포맷팅
                    dto.setFileSize(formatFileSize(file.getFileSize()));

                    return dto;
                }).collect(Collectors.toList());

        dashboardDto.setPendingFiles(mappedFiles);

        return dashboardDto;
    }

    private String formatFileSize(long size) {
        if (size <= 0)
            return "0 B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new java.text.DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " "
                + units[digitGroups];
    }
}
