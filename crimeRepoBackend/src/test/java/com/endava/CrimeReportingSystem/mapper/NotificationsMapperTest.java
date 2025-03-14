package com.endava.CrimeReportingSystem.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.endava.CrimeReportingSystem.entity.Notifications;
import com.endava.CrimeReportingSystem.entity.dto.NotificationsDTO;

class NotificationsMapperTest {

    private final NotificationsMapper notificationsMapper = new NotificationsMapper();

    @Test
    void testNotificationsToNotificationsDTO_Success() {
       
        Notifications notification = new Notifications();
        notification.setNotificationId(1);
        notification.setNotificationTitle("Test Notification");
        notification.setNotificationMessage("This is a test message.");
        notification.setCreatedAt(LocalDateTime.of(2023, 10, 1, 10, 30));
        notification.setIsActive(true);

        NotificationsDTO notificationsDTO = notificationsMapper.notificationsToNotificationsDTO(notification);

        assertEquals(notification.getNotificationId(), notificationsDTO.notificationId());
        assertEquals(notification.getNotificationTitle(), notificationsDTO.notificationTitle());
        assertEquals(notification.getNotificationMessage(), notificationsDTO.notificationMessage());
        assertEquals(notification.getCreatedAt(), notificationsDTO.createdAt());
        assertEquals(notification.getIsActive(), notificationsDTO.isActive());
    }

   

    @Test
    void testNotificationsDTOToNotifications_Success() {
        NotificationsDTO notificationsDTO = new NotificationsDTO(1,"Test Notification","This is a test message.",LocalDateTime.of(2023, 10, 1, 10, 30),LocalDateTime.of(2023, 10, 1, 10, 30),true,"",null);
       

        Notifications notification = notificationsMapper.notificationsDTOToNotifications(notificationsDTO);

        assertEquals(notificationsDTO.notificationId(), notification.getNotificationId());
        assertEquals(notificationsDTO.notificationTitle(), notification.getNotificationTitle());
        assertEquals(notificationsDTO.notificationMessage(), notification.getNotificationMessage());
        assertEquals(notificationsDTO.createdAt(), notification.getCreatedAt());
    }

    
}
