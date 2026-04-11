package com.metanet.myddareungi.domain.notification.service;

import java.util.List;

import com.metanet.myddareungi.domain.notification.model.Notification;

public interface INotificationService {
    List<Notification> findAll();
    List<Notification> findAllById(long userId);
    
    void markAllRead();
    void markAsRead(long notificationId);
    void deleteById(long notificationId);
    
    void insert(long userId, String notificationType, String message, long fileId );
}
