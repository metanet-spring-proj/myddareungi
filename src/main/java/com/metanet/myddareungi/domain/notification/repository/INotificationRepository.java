
package com.metanet.myddareungi.domain.notification.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.metanet.myddareungi.domain.notification.model.Notification;

@Mapper
public interface INotificationRepository { 
    List<Notification> findAll();
    List<Notification> findAllById(long userId);
    
    
    void markAllRead();
    void markAsRead(long notificationId);
    void deleteById(long notificationId);
    
    void insert(Notification notification);
    
    
}
