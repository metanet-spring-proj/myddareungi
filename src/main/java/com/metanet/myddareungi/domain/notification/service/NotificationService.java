package com.metanet.myddareungi.domain.notification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metanet.myddareungi.domain.notification.model.Notification;
import com.metanet.myddareungi.domain.notification.repository.INotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
	private final INotificationRepository notificationRepository;
	private final SseEmitterService sseEmitterService;

	@Override
	public List<Notification> findAll() {
		return notificationRepository.findAll();
	}

	@Override
	public List<Notification> findAllById(long userId){
		return notificationRepository.findAllById(userId);
	}

	@Override
	public List<Notification> findUnreadById(long userId) {
		return notificationRepository.findUnreadById(userId);
	}


	@Override
	public void markAllRead(long userId) {
		notificationRepository.markAllRead(userId);

	}

	@Override
	public void markAsRead(long notificationId) {
		notificationRepository.markAsRead(notificationId);
	}

	@Override
	public void deleteById(long notificationId) {
		notificationRepository.deleteById(notificationId);
	}

	@Override
	public void insert(long userId, String notificationType, String message, long fileId) {
		System.out.println("notification generate");
		Notification notification = new Notification();
		notification.setUserId(userId);
		notification.setNotificationType(notificationType);
		notification.setMessage(message);
		notification.setFileId(fileId);
		notificationRepository.insert(notification);

		// SSE 실시간 전송
		sseEmitterService.sendToUser(userId, notification);
	}

	@Override
	public void setStatus(long fileId, String status) {
		Notification notification = notificationRepository.findByFileId(fileId);
		notificationRepository.setStatus(status, notification.getNotificationId());
	}

}
