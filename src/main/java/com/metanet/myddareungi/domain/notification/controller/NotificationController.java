package com.metanet.myddareungi.domain.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metanet.myddareungi.config.CustomUserDetails;
import com.metanet.myddareungi.domain.notification.model.Notification;
import com.metanet.myddareungi.domain.notification.service.INotificationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
	private final INotificationService notificationService;

	
	// 알림 목록 조회
	@GetMapping
	public ResponseEntity<List<Notification>> getNotifications() {
		List<Notification> notifications = notificationService.findAll();
		return ResponseEntity.ok(notifications);
	}
	
	@GetMapping
	public ResponseEntity<List<Notification>> getMyNotifications(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		long userId = userDetails.getUserId();
		
		List<Notification> notifications = notificationService.findAllbyId(userId);
		return ResponseEntity.ok(notifications);
	}
	

	// 알림 읽음 처리
	@PatchMapping("/{notificationId}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable long notificationId) {
		notificationService.markAsRead(notificationId);
		return ResponseEntity.ok().build();
	}
	
	// 알림 읽음 처리
	@PatchMapping("/read")
	public ResponseEntity<Void> markReadAll() {
		notificationService.markAllRead();
		return ResponseEntity.ok().build();
	}

	// 알림 삭제
	@DeleteMapping("/{notificationId}")
	public ResponseEntity<Void> deleteNotification(
			@PathVariable long notificationId) {

		notificationService.deleteById(notificationId);
		return ResponseEntity.noContent().build();
	}
	
	
}
