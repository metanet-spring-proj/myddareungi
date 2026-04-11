
package com.metanet.myddareungi.domain.notification.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.metanet.myddareungi.domain.notification.model.Notification;

@Mapper
public interface INotificationRepository { 
    List<Notification> findAll();
    List<Notification> findAllById(long userId);
    List<Notification> findUnreadById(@Param("userId") long userId);
    
    void markAllRead(@Param("userId") long userId);
    void markAsRead(long notificationId);
    void deleteById(long notificationId);
    
    void insert(Notification notification);
    
    
    Notification findByFileId(long fileId);
    
    void setStatus(@Param("status") String status,
    		@Param("notificationId") long notificationId);
}
