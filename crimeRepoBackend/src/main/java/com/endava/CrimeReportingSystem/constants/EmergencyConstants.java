package com.endava.CrimeReportingSystem.constants;

public enum EmergencyConstants {

    // All messages for service impl
    SUCCESS_EMERGENCY_NUMBER_ADDED("Emergency number added successfully"),
    SUCCESS_EMERGENCY_NUMBER_UPDATED("Emergency number updated successfully"),
    ERROR_FAILED_TO_ADD("Failed to add emergency number"),
    ERROR_FAILED_TO_UPDATE("Failed to update emergency number"),
    
    // Messages for controller responses
    ERROR_EMERGENCY_NUMBERS_NOT_FOUND("No emergency numbers found"),
   
    ERROR_FAILED_TO_DELETE("Failed to delete emergency number"),
    ERROR_UNEXPECTED_ADDING("An unexpected error occurred while adding emergency number."),
    ERROR_UNEXPECTED_FETCHING("An unexpected error occurred while fetching emergency numbers."),
    ERROR_UNEXPECTED_UPDATING("An unexpected error occurred while updating emergency number."),
    ERROR_UNEXPECTED_DELETING("An unexpected error occurred while deleting emergency number."),

    // Success messages
    SUCCESS_EMERGENCY_NUMBER_DELETED("Emergency number deleted successfully");

    // Constants for URLs
    public static final String EMERGENCY_BASE_URL = "/crime-reporting-system/emergency";
    public static final String  ADD_EMERGENCY_NUMBER = "/add";
    public static final String FETCH_ALL_EMERGENCY_NUMBERS = "/all";
    public static final String UPDATE_EMERGENCY_NUMBER = "/update";
    public static final String DELETE_EMERGENCY_NUMBER = "/delete/{id}";

    private final String value;

    EmergencyConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
