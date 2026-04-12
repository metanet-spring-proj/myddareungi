package com.metanet.myddareungi.domain.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.metanet.myddareungi.domain.notification.model.Notification;
import com.metanet.myddareungi.domain.notification.repository.INotificationRepository;


@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
	@Mock
	INotificationRepository notificationRepository;

	@Mock
	SseEmitterService sseEmitterService;

	@InjectMocks
	NotificationService notificationService;
	

    //insert() 

    @Test
    @DisplayName("insert() - Notification 필드가 저장된다")
    void insert_필드설정_정상() {
        // when
        notificationService.insert(10L, "FILE UPLOAD", "CSV파일이 업로드 되었습니다.", 1L);

        // then
        verify(notificationRepository).insert(argThat(n ->
                n.getUserId() == 10L &&
                n.getNotificationType().equals("FILE UPLOAD") &&
                n.getMessage().equals("CSV파일이 업로드 되었습니다.") &&
                n.getFileId() == 1L
        ));
    }

    @Test
    @DisplayName("insert() - 저장 후 SSE 실시간 전송이 호출")
    void insert_SSE전송_호출() {
        // when
        notificationService.insert(10L, "FILE UPLOAD", "CSV파일이 업로드 되었습니다.", 1L);

        // then
        verify(sseEmitterService, times(1)).sendToUser(eq(10L), any(Notification.class));
    }

    //setStatus()

    @Test
    @DisplayName("setStatus() - 올바른 notificationId로 상태가 변경")
    void setStatus_정상처리() {
        // given
        Notification notification = new Notification();
        notification.setNotificationId(5L);
        when(notificationRepository.findByFileId(1L)).thenReturn(notification);

        // when
        notificationService.setStatus(1L, "APPROVED");

        // then
        verify(notificationRepository).setStatus("APPROVED", 5L);
    }
	
}

