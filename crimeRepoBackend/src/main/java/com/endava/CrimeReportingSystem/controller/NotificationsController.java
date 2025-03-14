package com.endava.CrimeReportingSystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.NotificationsConstants;
import com.endava.CrimeReportingSystem.entity.dto.NotificationsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.NotificationsServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;


@RestController
@RequestMapping(NotificationsConstants.BASE_NOTIFICATIONS_URL)
@Slf4j  // Logger created automatically
public class NotificationsController {

	private final NotificationsServiceImpl notificationsServiceImpl;

    // Constructor injection
    public NotificationsController(NotificationsServiceImpl notificationsServiceImpl) {
        this.notificationsServiceImpl = notificationsServiceImpl;
    }

    private final Sinks.Many<Map<String, String> >  notificationsSink = Sinks.many().multicast().onBackpressureBuffer();

    
 /**
  * sending alert push notifications
  * @param notificationsDTO
  * @return
  */
    @PostMapping (path=NotificationsConstants.EMERGENCY_ALERT_NOTIFICATION_PATH, consumes = {CommonConstants.APPLICATION_JSON})
     public String publishNotification(@RequestBody Map<String, String> notificationData ){
    	
    	Map<String , String> updatedNotificationData=notificationsServiceImpl.getEmergencyAlertNotification(notificationData);
        notificationsSink.tryEmitNext(updatedNotificationData);
        return "Notification sent " +updatedNotificationData.get("notificationMessage");
    }
    
    
    /**
     * receiving alert push notifications
     * @param notificationsDTO
     * @return
     */
    @GetMapping (path=NotificationsConstants.RECEIVE_ALERT_NOTIFICATIONS_PATH,produces  = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String> > streamNotification(){
      
      return notificationsSink.asFlux();
    }
    
    /**
     * End point to save a notification for a user.
     * @param notificationsDTO The notification data to be saved
     * @return A success or failure response based on notification save status
     */
    @PostMapping(path =NotificationsConstants.SEND_NOTIFICATION_PATH, consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveUserNotifications(@RequestBody NotificationsDTO notificationsDTO) {
        log.debug("NotificationsController::saveUserNotifications() - Saving notification for userType: {}",notificationsDTO.toUserType());

        try {
            Boolean status = notificationsServiceImpl.saveNotification(notificationsDTO);
            if (status) {
                log.debug("NotificationsController::saveUserNotifications() - Notification successfully saved for userType: {}", notificationsDTO.toUserType());
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                log.debug("NotificationsController::saveUserNotifications() - Failed to save notification for userType: {}", notificationsDTO.toUserType());
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NotificationsController::saveUserNotifications() - Error occurred while saving notification for userType: {}. Error: {}", notificationsDTO.toUserType(), e.getMessage());
            return new ResponseEntity<>(NotificationsConstants.ERROR_SAVING_NOTIFICATION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
   
    /**
     * End point to fetch notifications for a specific user by userId.
     * @param userId The ID of the user whose notifications are to be fetched
     * @return A list of notifications for the specified user, or an error message if none are found
     */
    @GetMapping(path =NotificationsConstants.GET_NOTIFICATIONS_BY_USER_ID_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> fetchNotificationByUserId(@PathVariable int userId) {
        log.debug("NotificationsController::getReportById() - Fetching notifications for userId: {}", userId);

        try {
            ApiGenericResponse<List<NotificationsDTO>> notifications = notificationsServiceImpl.getAllNotificationsByUserId(userId);

            if (notifications.getData() != null) {
                log.debug("NotificationsController::getReportById() - Successfully fetched notifications for userId: {}", userId);
                return new ResponseEntity<>(notifications.getData(), HttpStatus.OK);
            } else {
                log.debug("NotificationsController::getReportById() - No notifications found for userId: {}", userId);
                return new ResponseEntity<>(notifications.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NotificationsController::getReportById() - Error occurred while fetching notifications for userId: {}. Error: {}", userId, e.getMessage());
            return new ResponseEntity<>(NotificationsConstants.ERROR_FETCHING_NOTIFICATIONS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to update the status of a notification.
     * @param notificationId The ID of the notification to be updated
     * @return A success or failure response based on the notification status update
     */
    @PutMapping(path = NotificationsConstants.UPDATE_NOTIFICATION_PATH)
    public ResponseEntity<?> updateNotificationByNotificationId(@PathVariable int notificationId) {
        log.debug("NotificationsController::updateReport() - Updating notification status for notificationId: {}", notificationId);

        try {
            Boolean updateStatus = notificationsServiceImpl.updateNotification(notificationId);

            if (updateStatus) {
                log.debug("NotificationsController::updateReport() - Successfully updated notification status for notificationId: {}", notificationId);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                log.debug("NotificationsController::updateReport() - Failed to update notification status for notificationId: {}", notificationId);
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NotificationsController::updateReport() - Error occurred while updating notification status for notificationId: {}. Error: {}", notificationId, e.getMessage());
            return new ResponseEntity<>(NotificationsConstants.ERROR_UPDATING_NOTIFICATION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * End point to clear notifications for a specific user by userId.
     * @param userId The ID of the user whose notifications are to be cleared
     * @return A success or failure response based on the status of the operation
     */
    @PutMapping(path = NotificationsConstants.CLEAR_NOTIFICATIONS_BY_USER_ID_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> clearNotificationsByUserId(@PathVariable int userId) {
        log.debug("NotificationsController::clearNotificationsByUserId() - Clearing notifications for userId: {}", userId);

        try {
            Boolean status = notificationsServiceImpl.clearNotificationByUserId(userId);

            if (status) {
                log.debug("NotificationsController::clearNotificationsByUserId() - Successfully cleared notifications for userId: {}", userId);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                log.debug("NotificationsController::clearNotificationsByUserId() - No notifications found for userId: {}", userId);
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NotificationsController::clearNotificationsByUserId() - Error occurred while clearing notifications for userId: {}. Error: {}", userId, e.getMessage());
            return new ResponseEntity<>(NotificationsConstants.ERROR_CLEARING_NOTIFICATIONS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
