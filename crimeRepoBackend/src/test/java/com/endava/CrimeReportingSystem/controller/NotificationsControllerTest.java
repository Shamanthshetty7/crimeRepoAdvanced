package com.endava.CrimeReportingSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.endava.CrimeReportingSystem.constants.NotificationsConstants;
import com.endava.CrimeReportingSystem.entity.dto.NotificationsDTO;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.NotificationsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class NotificationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private NotificationsServiceImpl notificationsServiceImpl;

    @InjectMocks
    private NotificationsController notificationsController;

    private ObjectMapper objectMapper;
    private NotificationsDTO notificationDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(notificationsController).build();
        objectMapper.registerModule(new JavaTimeModule());
         notificationDTO = new NotificationsDTO(
        	    0, 
        	    "Test Notification", 
        	    "This is a test message.", 
        	    LocalDateTime.now(), 
        	    LocalDateTime.now(), 
        	    true, 
        	    "Admin", 
        	   null
        	);       
    }

    // Test for publishNotification
    @Test
    void testPublishNotification_Success() throws Exception {
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("notificationMessage", "Test Alert");

        when(notificationsServiceImpl.getEmergencyAlertNotification(any())).thenReturn(notificationData);

        mockMvc.perform(post("/crime-reporting-system/notifications/emergencyAlertNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Notification sent Test Alert"));
    }

    // Test for saveUserNotifications (Success)
    @Test
    void testSaveUserNotifications_Success() throws Exception {
        when(notificationsServiceImpl.saveNotification(any(NotificationsDTO.class))).thenReturn(true);

        mockMvc.perform(post("/crime-reporting-system/notifications/sendNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    // Test for saveUserNotifications (Failure)
    @Test
    void testSaveUserNotifications_Failure() throws Exception {
        when(notificationsServiceImpl.saveNotification(any(NotificationsDTO.class))).thenReturn(false);

        mockMvc.perform(post("/crime-reporting-system/notifications/sendNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(false));
    }

    // Test for fetchNotificationByUserId (Success)
    @Test
    void testFetchNotificationByUserId_Success() throws Exception {
        List<NotificationsDTO> notificationsList = new ArrayList<>();
        notificationsList.add(notificationDTO);

        ApiGenericResponse<List<NotificationsDTO>> response = new ApiGenericResponse<>(null, null);
        response.setData(notificationsList);
        when(notificationsServiceImpl.getAllNotificationsByUserId(anyInt())).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/notifications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].notificationTitle").value("Test Notification"));
    }

    // Test for fetchNotificationByUserId (No Notifications Found)
    @Test
    void testFetchNotificationByUserId_NotFound() throws Exception {
        ApiGenericResponse<List<NotificationsDTO>> response = new ApiGenericResponse<>(null, null);
        response.setMessage("No notifications found");
        when(notificationsServiceImpl.getAllNotificationsByUserId(anyInt())).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/notifications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No notifications found"));
    }

    // Test for updateNotificationByNotificationId (Success)
    @Test
    void testUpdateNotificationByNotificationId_Success() throws Exception {
        when(notificationsServiceImpl.updateNotification(anyInt())).thenReturn(true);

        mockMvc.perform(put("/crime-reporting-system/notifications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    // Test for updateNotificationByNotificationId (Failure)
    @Test
    void testUpdateNotificationByNotificationId_Failure() throws Exception {
        when(notificationsServiceImpl.updateNotification(anyInt())).thenReturn(false);

        mockMvc.perform(put("/crime-reporting-system/notifications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(false));
    }

    // Test for clearNotificationsByUserId (Success)
    @Test
    void testClearNotificationsByUserId_Success() throws Exception {
        when(notificationsServiceImpl.clearNotificationByUserId(anyInt())).thenReturn(true);

        mockMvc.perform(put("/crime-reporting-system/notifications/clear/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    // Test for clearNotificationsByUserId (Failure)
    @Test
    void testClearNotificationsByUserId_Failure() throws Exception {
        when(notificationsServiceImpl.clearNotificationByUserId(anyInt())).thenReturn(false);

        mockMvc.perform(put("/crime-reporting-system/notifications/clear/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(false));
    }
    


    // Test for saveUserNotifications (Exception Handling)
    @Test
    void testSaveUserNotifications_Exception() throws Exception {
        when(notificationsServiceImpl.saveNotification(any(NotificationsDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/crime-reporting-system/notifications/sendNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while saving notification."));
    }

    // Test for fetchNotificationByUserId (Exception Handling)
    @Test
    void testFetchNotificationByUserId_Exception() throws Exception {
        when(notificationsServiceImpl.getAllNotificationsByUserId(anyInt())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/crime-reporting-system/notifications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while fetching notifications."));
    }

    // Test for updateNotificationByNotificationId (Exception Handling)
    @Test
    void testUpdateNotificationByNotificationId_Exception() throws Exception {
        when(notificationsServiceImpl.updateNotification(anyInt())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(put("/crime-reporting-system/notifications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value(NotificationsConstants.ERROR_UPDATING_NOTIFICATION.getMessage()));
    }

    // Test for clearNotificationsByUserId (Exception Handling)
    @Test
    void testClearNotificationsByUserId_Exception() throws Exception {
        when(notificationsServiceImpl.clearNotificationByUserId(anyInt())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(put("/crime-reporting-system/notifications/clear/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while clearing notifications."));
    }
}
