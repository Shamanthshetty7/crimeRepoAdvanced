package com.endava.CrimeReportingSystem.mapper;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.Notifications;
import com.endava.CrimeReportingSystem.entity.dto.NotificationsDTO;

@Component
public class NotificationsMapper {

   
    public NotificationsDTO notificationsToNotificationsDTO(Notifications notification) {
        return new NotificationsDTO(notification.getNotificationId(),notification.getNotificationTitle(),notification.getNotificationMessage(),notification.getCreatedAt(),null,notification.getIsActive(),null,null);
        
      
       
    }


    public Notifications notificationsDTOToNotifications(NotificationsDTO notificationsDTO) {
        Notifications notification = new Notifications();
        notification.setNotificationId(notificationsDTO.notificationId());
        notification.setNotificationTitle(notificationsDTO.notificationTitle());
        notification.setNotificationMessage(notificationsDTO.notificationMessage());
        notification.setCreatedAt(notificationsDTO.createdAt());
        return notification;
    }
}
