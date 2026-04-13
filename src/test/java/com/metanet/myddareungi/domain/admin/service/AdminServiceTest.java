package com.metanet.myddareungi.domain.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.metanet.myddareungi.domain.admin.dto.AdminDashboardDto;
import com.metanet.myddareungi.domain.admin.repository.IAdminRepository;
import com.metanet.myddareungi.domain.files.model.UploadFile;
import com.metanet.myddareungi.domain.files.service.IUploadFileService;
import com.metanet.myddareungi.domain.member.model.Member;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private IAdminRepository adminRepository;

    @Mock
    private IUploadFileService uploadFileService;

    @InjectMocks
    private AdminService adminService;

    @Test
    @DisplayName("관리자 정보가 존재하면 대시보드 DTO에 관리자 정보가 채워진다")
    void getDashboardData_adminExists_setsAdminInfo() {
        // given
        long userId = 1L;
        Member admin = Member.builder()
                .userId(userId)
                .loginId("admin")
                .userName("홍길동")
                .email("admin@metanet.com")
                .build();

        given(adminRepository.getAdminInfo(userId)).willReturn(admin);
        given(adminRepository.countPendingFiles()).willReturn(5);
        given(adminRepository.countTodayUploads()).willReturn(3);
        given(adminRepository.getPendingFilesPaged(0, 10)).willReturn(new ArrayList<>());

        // when
        AdminDashboardDto result = adminService.getDashboardData(userId, 0, 10);

        // then
        assertThat(result.getAdminId()).isEqualTo(userId);
        assertThat(result.getAdminName()).isEqualTo("홍길동");
        assertThat(result.getAdminEmail()).isEqualTo("admin@metanet.com");
        assertThat(result.getPendingCount()).isEqualTo(5);
        assertThat(result.getTodayUploadCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("관리자 정보가 없으면 기본값으로 대시보드 DTO가 채워진다")
    void getDashboardData_adminNotFound_usesDefaultValues() {
        // given
        long userId = 99L;
        given(adminRepository.getAdminInfo(userId)).willReturn(null);
        given(adminRepository.countPendingFiles()).willReturn(0);
        given(adminRepository.countTodayUploads()).willReturn(0);
        given(adminRepository.getPendingFilesPaged(0, 10)).willReturn(new ArrayList<>());

        // when
        AdminDashboardDto result = adminService.getDashboardData(userId, 0, 10);

        // then
        assertThat(result.getAdminName()).isEqualTo("관리자");
        assertThat(result.getAdminEmail()).isEqualTo("admin@example.com");
    }

    @Test
    @DisplayName("pendingCount가 0이면 totalPages는 1이다")
    void getDashboardData_zeroPending_totalPagesIsOne() {
        // given
        long userId = 1L;
        given(adminRepository.getAdminInfo(userId)).willReturn(
                Member.builder().userId(userId).userName("관리자").email("a@b.com").build());
        given(adminRepository.countPendingFiles()).willReturn(0);
        given(adminRepository.countTodayUploads()).willReturn(0);
        given(adminRepository.getPendingFilesPaged(0, 10)).willReturn(new ArrayList<>());

        // when
        AdminDashboardDto result = adminService.getDashboardData(userId, 0, 10);

        // then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getCurrentPage()).isEqualTo(0);
    }

    @Test
    @DisplayName("pendingCount와 size에 따라 totalPages가 올바르게 계산된다")
    void getDashboardData_paginationCalculation_correct() {
        // given
        long userId = 1L;
        given(adminRepository.getAdminInfo(userId)).willReturn(
                Member.builder().userId(userId).userName("관리자").email("a@b.com").build());
        given(adminRepository.countPendingFiles()).willReturn(25);
        given(adminRepository.countTodayUploads()).willReturn(2);
        given(adminRepository.getPendingFilesPaged(0, 10)).willReturn(new ArrayList<>());

        // when
        AdminDashboardDto result = adminService.getDashboardData(userId, 0, 10);

        // then
        // ceil(25/10) = 3
        assertThat(result.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("page가 totalPages를 초과하면 마지막 페이지로 조정된다")
    void getDashboardData_pageExceedsTotalPages_clampsToLast() {
        // given
        long userId = 1L;
        given(adminRepository.getAdminInfo(userId)).willReturn(
                Member.builder().userId(userId).userName("관리자").email("a@b.com").build());
        given(adminRepository.countPendingFiles()).willReturn(10);
        given(adminRepository.countTodayUploads()).willReturn(1);
        // totalPages = ceil(10/10) = 1, so page is clamped to 0
        given(adminRepository.getPendingFilesPaged(0, 10)).willReturn(new ArrayList<>());

        // when
        AdminDashboardDto result = adminService.getDashboardData(userId, 99, 10);

        // then
        assertThat(result.getCurrentPage()).isEqualTo(0); // clamped to totalPages-1
    }

    @Test
    @DisplayName("파일 목록을 조회하여 PendingFileDto 리스트로 변환한다")
    void getDashboardData_filesMappedToPendingFileDto() {
        // given
        long userId = 1L;
        given(adminRepository.getAdminInfo(userId)).willReturn(
                Member.builder().userId(userId).userName("관리자").email("a@b.com").build());
        given(adminRepository.countPendingFiles()).willReturn(1);
        given(adminRepository.countTodayUploads()).willReturn(0);

        UploadFile file = new UploadFile();
        file.setFileId(10L);
        file.setFileName("test.csv");
        file.setStatus("PENDING");
        file.setUploadedAt(new Timestamp(System.currentTimeMillis()));
        file.setUploaderId(1L);
        file.setFileSize(1024L);

        given(adminRepository.getPendingFilesPaged(0, 10)).willReturn(List.of(file));

        // when
        AdminDashboardDto result = adminService.getDashboardData(userId, 0, 10);

        // then
        assertThat(result.getPendingFiles()).hasSize(1);
        assertThat(result.getPendingFiles().get(0).getFileName()).isEqualTo("test.csv");
        assertThat(result.getPendingFiles().get(0).getStatus()).isEqualTo("PENDING");
        assertThat(result.getPendingFiles().get(0).getFileSize()).isEqualTo("1 KB");
    }

    @Test
    @DisplayName("파일 업로드 날짜가 null이면 '-'로 표시된다")
    void getDashboardData_nullUploadedAt_displaysDash() {
        // given
        long userId = 1L;
        given(adminRepository.getAdminInfo(userId)).willReturn(
                Member.builder().userId(userId).userName("관리자").email("a@b.com").build());
        given(adminRepository.countPendingFiles()).willReturn(1);
        given(adminRepository.countTodayUploads()).willReturn(0);

        UploadFile file = new UploadFile();
        file.setFileId(11L);
        file.setFileName("nodate.csv");
        file.setStatus("PENDING");
        file.setUploadedAt(null); // null date
        file.setUploaderId(1L);
        file.setFileSize(512L);

        given(adminRepository.getPendingFilesPaged(0, 10)).willReturn(List.of(file));

        // when
        AdminDashboardDto result = adminService.getDashboardData(userId, 0, 10);

        // then
        assertThat(result.getPendingFiles().get(0).getUploadDate()).isEqualTo("-");
    }
}
