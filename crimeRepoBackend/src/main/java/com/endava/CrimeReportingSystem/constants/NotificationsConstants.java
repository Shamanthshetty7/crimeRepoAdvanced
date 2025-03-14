package com.endava.CrimeReportingSystem.constants;

public enum NotificationsConstants {

    // Generic messages
    ERROR_FETCHING_NOTIFICATIONS("An unexpected error occurred while fetching notifications."),
    ERROR_NO_NOTIFICATIONS_FOUND("Notifications not found"),
    ERROR_SAVING_NOTIFICATION("An unexpected error occurred while saving notification."),
    ERROR_UPDATING_NOTIFICATION("An unexpected error occurred while updating notification."),
    ERROR_CLEARING_NOTIFICATIONS("An unexpected error occurred while clearing notifications."),
   
    ERROR_SENDING_NOTIFICATION("An unexpected error occurred while sending notification.");

    // Path variables
    public static final String BASE_NOTIFICATIONS_URL = "/crime-reporting-system/notifications";
    public static final String EMERGENCY_ALERT_NOTIFICATION_PATH = "/emergencyAlertNotification";
    public static final String RECEIVE_ALERT_NOTIFICATIONS_PATH = "/recieveAlertNotifications";
    public static final String SEND_NOTIFICATION_PATH = "/sendNotification";
    public static final String GET_NOTIFICATIONS_BY_USER_ID_PATH = "/{userId}";
    public static final String UPDATE_NOTIFICATION_PATH = "/{notificationId}";
    public static final String CLEAR_NOTIFICATIONS_BY_USER_ID_PATH = "/clear/{userId}";

    private final String value;

    NotificationsConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
