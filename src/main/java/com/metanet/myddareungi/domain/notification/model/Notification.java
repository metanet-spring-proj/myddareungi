package com.metanet.myddareungi.domain.notification.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Notification {
	long notificationId;
	long userId;
	String notificationType;
	String message;
	long fileId;
	int isRead;
	Timestamp createdAt;
}
