package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.CrimeReportingSystem.constants.NotificationsConstants;
import com.endava.CrimeReportingSystem.entity.Notifications;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.NotificationsDTO;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.mapper.NotificationsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.NotificationsRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.utility.ExternalAPIs;

@ExtendWith(MockitoExtension.class)
class NotificationsServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private NotificationsRepository notificationsRepository;

    @Mock
    private NotificationsMapper notificationsMapper;

    @Mock
    private UsersMapper usersMapper;

    @Mock
    private ExternalAPIs externalAPIs;

    @InjectMocks
    private NotificationsServiceImpl notificationsService;

    private Users user;
    private Notifications notification;
    private NotificationsDTO notificationDTO;
    private UsersDTO usersDTO;
    @BeforeEach
    void setUp() {
        user = new Users();
        user.setUserId(1);
        user.setUserName("John Doe");
        user.setUserType(UserType.Investigator);
       
        usersDTO=new UsersDTO();
        usersDTO.setUserId(1);
        usersDTO.setUserName("John Doe");
        usersDTO.setUserType(UserType.Investigator);

        notification = new Notifications();
        notification.setNotificationId(1);
        notification.setNotificationTitle("Test Notification");
        notification.setNotificationMessage("This is a test notification.");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsActive(true);
        notification.setUser(user);

        notificationDTO = new NotificationsDTO(1,"Test Notification","This is a test notification.",LocalDateTime.now(),LocalDateTime.now(),true,"",usersDTO);
       
    }

 // Test case: Save notification for all Investigators (Success)
    @Test
    void testSaveNotification_Success_Investigator() {
        notificationDTO = new NotificationsDTO(1,"Test Notification","This is a test notification.",LocalDateTime.now(),LocalDateTime.now(),true,"Investigator",usersDTO);

        List<Users> users = List.of(user);

        when(usersRepository.findByUserType(UserType.Investigator)).thenReturn(users);

        boolean result = notificationsService.saveNotification(notificationDTO);

        assertTrue(result);
    }

    // Test case: No Investigators found (Failure)
    @Test
    void testSaveNotification_Failure_Investigator_NoUsers() {
        notificationDTO = new NotificationsDTO(1,"Test Notification","This is a test notification.",LocalDateTime.now(),LocalDateTime.now(),true,"Investigator",usersDTO);

        when(usersRepository.findByUserType(UserType.Investigator)).thenReturn(new ArrayList<>());

        boolean result = notificationsService.saveNotification(notificationDTO);

        assertFalse(result);
        verify(notificationsRepository, never()).saveAll(anyList());
    }

    // Test case: Save notification for all Informants (Success)
    @Test
    void testSaveNotification_Success_Informant() {
        notificationDTO = new NotificationsDTO(1,"Test Notification","This is a test notification.",LocalDateTime.now(),LocalDateTime.now(),true,"Informant",usersDTO);
        user.setUserType(UserType.Informant);
        List<Users> users = List.of(user);

        when(usersRepository.findByUserType(UserType.Informant)).thenReturn(users);

        boolean result = notificationsService.saveNotification(notificationDTO);

        assertTrue(result);
    }

    // Test case: No Informants found (Failure)
    @Test
    void testSaveNotification_Failure_Informant_NoUsers() {
        notificationDTO = new NotificationsDTO(1,"Test Notification","This is a test notification.",LocalDateTime.now(),LocalDateTime.now(),true,"Informant",usersDTO);

        when(usersRepository.findByUserType(UserType.Informant)).thenReturn(new ArrayList<>());

        boolean result = notificationsService.saveNotification(notificationDTO);

        assertFalse(result);
        verify(notificationsRepository, never()).saveAll(anyList());
    }

    // Test case: Save notification for a specific user (Success)
    @Test
    void testSaveNotification_Success_SpecificUser() {
        notificationDTO = new NotificationsDTO(1,"Test Notification","This is a test notification.",LocalDateTime.now(),LocalDateTime.now(),true,"SpecificUser",usersMapper.usersToUsersDTO(user));

    	 

        Notifications notification = new Notifications();
        when(notificationsMapper.notificationsDTOToNotifications(notificationDTO)).thenReturn(notification);
        when(usersMapper.usersDTOtoUsers(notificationDTO.user())).thenReturn(user);
        when(notificationsRepository.save(notification)).thenReturn(notification);
        boolean result = notificationsService.saveNotification(notificationDTO);

        assertTrue(result);
    }
    // Test for getAllNotificationsByUserId
    @Test
    void testGetAllNotificationsByUserId_Success() {
        List<Notifications> notificationsList = List.of(notification);
        when(notificationsRepository.findByUserUserId(anyInt())).thenReturn(notificationsList);
        when(notificationsMapper.notificationsToNotificationsDTO(any())).thenReturn(notificationDTO);

      
        ApiGenericResponse<List<NotificationsDTO>> response = notificationsService.getAllNotificationsByUserId(1);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals("Test Notification", response.getData().get(0).notificationTitle());
    }

    @Test
    void testGetAllNotificationsByUserId_NoNotifications() {
   
        when(notificationsRepository.findByUserUserId(anyInt())).thenReturn(null);

     
        ApiGenericResponse<List<NotificationsDTO>> response = notificationsService.getAllNotificationsByUserId(1);

    
        assertNull(response.getData());
        assertEquals(NotificationsConstants.ERROR_NO_NOTIFICATIONS_FOUND.getMessage(), response.getMessage());
    }

    // Test for updateNotification
    @Test
    void testUpdateNotification_Success() {
        when(notificationsRepository.findById(anyInt())).thenReturn(java.util.Optional.of(notification));
        when(notificationsRepository.save(any())).thenReturn(notification);

        boolean result = notificationsService.updateNotification(1);

        assertTrue(result);
        assertFalse(notification.getIsActive());
        verify(notificationsRepository).save(any());
    }

    @Test
    void testUpdateNotification_Failure() {
        when(notificationsRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        boolean result = notificationsService.updateNotification(1);

        assertFalse(result);
        verify(notificationsRepository, never()).save(any());
    }

    // Test for clearNotificationByUserId
    @Test
    void testClearNotificationByUserId_Success() {
        when(notificationsRepository.updateNotificationsStatusByUserId(anyInt())).thenReturn(1);

        boolean result = notificationsService.clearNotificationByUserId(1);

        assertTrue(result);
        verify(notificationsRepository).updateNotificationsStatusByUserId(anyInt());
    }

    @Test
    void testClearNotificationByUserId_Failure() {
        when(notificationsRepository.updateNotificationsStatusByUserId(anyInt())).thenReturn(0);

        boolean result = notificationsService.clearNotificationByUserId(1);

        assertFalse(result);
        verify(notificationsRepository).updateNotificationsStatusByUserId(anyInt());
    }

    // Test for getEmergencyAlertNotification
    @Test
    void testGetEmergencyAlertNotification_Success() {
        Map<String, String> inputData = new HashMap<>();
        inputData.put("userLatLong", "40.7128 -74.0060");
        when(externalAPIs.getAddressByLatLong(40.7128, -74.0060)).thenReturn("New York, USA");

        Map<String, String> result = notificationsService.getEmergencyAlertNotification(inputData);

        assertEquals("New York, USA", result.get("Address"));
    }
}
