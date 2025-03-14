package com.endava.CrimeReportingSystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.NewsConstants;
import com.endava.CrimeReportingSystem.constants.NotificationsConstants;
import com.endava.CrimeReportingSystem.entity.Notifications;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.NotificationsDTO;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.mapper.NotificationsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.NotificationsRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.NotificationsService;
import com.endava.CrimeReportingSystem.service.utility.ExternalAPIs;

@Service
public class NotificationsServiceImpl implements NotificationsService{

	 private final UsersRepository usersRepository;
	    private final NotificationsRepository notificationsRepository;
	    private final NotificationsMapper notificationsMapper;
	    private final UsersMapper usersMapper;
	    private final ExternalAPIs externalAPIs;

	    // Constructor injection
	    public NotificationsServiceImpl(UsersRepository usersRepository,
	                           NotificationsRepository notificationsRepository,
	                           NotificationsMapper notificationsMapper,
	                           UsersMapper usersMapper,
	                           ExternalAPIs externalAPIs) {
	        this.usersRepository = usersRepository;
	        this.notificationsRepository = notificationsRepository;
	        this.notificationsMapper = notificationsMapper;
	        this.usersMapper = usersMapper;
	        this.externalAPIs = externalAPIs;
	    }
	
	/** 
     * Method to save a notification based on the recipient user type (Investigator, Informant, or specific user).
     * Creates notifications for users of the specified type and saves them to the database.
     */
	@Override
	public Boolean saveNotification(NotificationsDTO notificationsDTO) {
		
		switch (notificationsDTO.toUserType()) {
	    case CommonConstants.INVESTIGATOR -> {
	        List<Users> users = usersRepository.findByUserType(UserType.Investigator);
	        if (users.isEmpty()) {
	            return false;
	        }
	        List<Notifications> notifications = users.stream().map((user) -> {
	            Notifications notification = new Notifications();
	            notification.setIsActive(true);
	            notification.setCreatedAt(LocalDateTime.now());
	            notification.setUpdatedAt(LocalDateTime.now());
	            notification.setNotificationTitle(notificationsDTO.notificationTitle());
	            notification.setNotificationMessage(notificationsDTO.notificationMessage());
	            notification.setUser(user);
	            return notification;
	        }).toList();
	        notificationsRepository.saveAll(notifications);
	        return true;
	    }
	    case CommonConstants.INFORMANT -> {
	        List<Users> users = usersRepository.findByUserType(UserType.Informant);
	        if (users.isEmpty()) {
	            return false;
	        }
	        List<Notifications> notifications = users.stream().map((user) -> {
	            Notifications notification = new Notifications();
	            notification.setIsActive(true);
	            notification.setCreatedAt(LocalDateTime.now());
	            notification.setUpdatedAt(LocalDateTime.now());
	            notification.setNotificationTitle(notificationsDTO.notificationTitle());
	            notification.setNotificationMessage(notificationsDTO.notificationMessage());
	            notification.setUser(user);
	            return notification;
	        }).toList();
	        notificationsRepository.saveAll(notifications);
	        return true;
	    }
	    default -> {
	        Notifications notification = notificationsMapper.notificationsDTOToNotifications(notificationsDTO);
	        notification.setUser(usersMapper.usersDTOtoUsers(notificationsDTO.user()));
	        notification.setIsActive(true);
	        notification.setCreatedAt(LocalDateTime.now());
	        notification.setUpdatedAt(LocalDateTime.now());
	        
	        Optional.ofNullable(notificationsRepository.save(notification)).orElseThrow(()->new RuntimeException(NotificationsConstants.ERROR_SAVING_NOTIFICATION.getMessage()));
	        return true;
	    }
	}

		
	}

	
	/** 
     * Method to retrieve all notifications for a specific user by their userId.
     * Filters active notifications and returns them in the response.
     */
	@Override
	public ApiGenericResponse<List<NotificationsDTO>>  getAllNotificationsByUserId(int userId) {
		ApiGenericResponse<List<NotificationsDTO>> response=new ApiGenericResponse<>(null,null);

		List<Notifications> allNotifications=notificationsRepository.findByUserUserId(userId);
		if(allNotifications==null) {
			response.setMessage(NotificationsConstants.ERROR_NO_NOTIFICATIONS_FOUND.getMessage());
			return response;
		}
		List<NotificationsDTO> allNotificationsDTOs= allNotifications
				.stream()
				.filter(Notifications::getIsActive)
				.map(notificationsMapper::notificationsToNotificationsDTO).toList();
		response.setData(allNotificationsDTOs);
		return response;
	}

	/** 
     * Method to update a notification by marking it as inactive (deleting it logically).
     */
	@Override
	public Boolean updateNotification(int notificationId) {
		Optional<Notifications> existingNotification=notificationsRepository.findById(notificationId);
		if(existingNotification.isEmpty()) {
			return false;
		}
		existingNotification.get().setIsActive(false);
		existingNotification.get().setUpdatedAt(LocalDateTime.now());
		
	Optional.ofNullable(notificationsRepository.save(existingNotification.get())).orElseThrow(()->new RuntimeException(NewsConstants.ERROR_SAVING_NEWS.getMessage()));
		return true;
	}
	
	@Override
	public Boolean clearNotificationByUserId(int userId) {
		int statusUpdate=notificationsRepository.updateNotificationsStatusByUserId(userId);
		
	 return statusUpdate!=0;
	}


	@Override
	public Map<String, String> getEmergencyAlertNotification(Map<String, String> notificationData) {

		var userLatLong=notificationData.get("userLatLong").trim().split(" ");
		notificationData.put("Address",externalAPIs.getAddressByLatLong(Double.parseDouble(userLatLong[0]) ,Double.parseDouble(userLatLong[1])));
		return notificationData;
	}

}
