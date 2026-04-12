package com.metanet.myddareungi.domain.fileupload.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.metanet.myddareungi.domain.files.model.UploadFile;
import com.metanet.myddareungi.domain.files.repository.IUploadFileRepository;
import com.metanet.myddareungi.domain.files.service.UploadFileService;
import com.metanet.myddareungi.domain.notification.service.INotificationService;


@ExtendWith(MockitoExtension.class)
public class UploadFIleServiceTest {
	@Mock
	IUploadFileRepository uploadFileRepository;

	@Mock
	INotificationService notificationService;

	@InjectMocks
	UploadFileService uploadFileService;

	// ── reviewFile() ───────────────────────────────────────────────────────────

	@Test
	@DisplayName("reviewFile() - APPROVED 처리 시 status, reviewedBy가 올바르게 저장된다")
	void reviewFile_APPROVED_정상처리() {
		// given
		UploadFile file = new UploadFile();
		file.setFileId(1L);
		file.setUploaderId(10L);
		file.setStatus("PENDING");
		when(uploadFileRepository.getFile(1L)).thenReturn(file);

		// when
		uploadFileService.reviewFile(1L, "APPROVED", 99L);

		// then - status, reviewedBy 가 올바르게 set 됐는지 확인
		verify(uploadFileRepository).reviewFile(argThat(f ->
		f.getStatus().equals("APPROVED") &&
		f.getReviewedBy() == 99L
				));
	}

	@Test
	@DisplayName("reviewFile() - REJECTED 처리 시 status, reviewedBy가 올바르게 저장된다")
	void reviewFile_REJECTED_정상처리() {
		// given
		UploadFile file = new UploadFile();
		file.setFileId(2L);
		file.setUploaderId(10L);
		file.setStatus("PENDING");
		when(uploadFileRepository.getFile(2L)).thenReturn(file);

		// when
		uploadFileService.reviewFile(2L, "REJECTED", 99L);

		// then
		verify(uploadFileRepository).reviewFile(argThat(f ->
		f.getStatus().equals("REJECTED") &&
		f.getReviewedBy() == 99L
				));
	}

	@Test
	@DisplayName("reviewFile() - 처리 후 알림(insert, setStatus)이 각 1회씩 호출된다")
	void reviewFile_알림_호출확인() {
		// given
		UploadFile file = new UploadFile();
		file.setFileId(1L);
		file.setUploaderId(10L);
		when(uploadFileRepository.getFile(1L)).thenReturn(file);

		// when
		uploadFileService.reviewFile(1L, "APPROVED", 99L);

		// then
		verify(notificationService, times(1))
		.insert(10L, "APPROVED", "파일이 [APPROVED] 처리 되었습니다.", 1L);
		verify(notificationService, times(1))
		.setStatus(1L, "APPROVED");
	}

	@Test
	@DisplayName("reviewFile() - 존재하지 않는 파일 ID - IllegalArgumentException 발생")
	void reviewFile_파일없음_NPE() {
		// given
		when(uploadFileRepository.getFile(999L)).thenReturn(null);

		// when & then
		assertThatThrownBy(() -> uploadFileService.reviewFile(999L, "APPROVED", 99L))
		.isInstanceOf(IllegalArgumentException.class);
	}

}
