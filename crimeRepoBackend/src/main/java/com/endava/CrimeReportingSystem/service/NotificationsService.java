package com.endava.CrimeReportingSystem.service;

import java.util.List;
import java.util.Map;

import com.endava.CrimeReportingSystem.entity.dto.NotificationsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface NotificationsService {

	public Boolean saveNotification(NotificationsDTO notificationsDTO);
	public ApiGenericResponse<List<NotificationsDTO>>  getAllNotificationsByUserId(int userId);
	public Boolean updateNotification(int notificationId);
	public  Boolean clearNotificationByUserId(int userId);
	public Map<String,String> getEmergencyAlertNotification(Map<String,String> notificationData);
}
