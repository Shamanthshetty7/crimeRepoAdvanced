package com.endava.CrimeReportingSystem.constants;

public enum KycApplicationConstants {

    // Generic messages
    ERROR_FETCHING_KYC_APPLICATION("An unexpected error occurred while fetching KYC application."),
    ERROR_NO_KYC_APPLICATION_FOUND("KYC application not found"),
    ERROR_SAVING_KYC_APPLICATION("An unexpected error occurred while saving KYC application."),
    ERROR_SAVING_KYC_IMAGES("An unexpected error occurred while saving KYC images."),
    ERROR_UPDATING_KYC_STATUS("An unexpected error occurred while updating KYC status."),
    ERROR_FETCHING_ALL_KYC_APPLICATIONS("An unexpected error occurred while fetching all KYC applications."),
    ERROR_KYC_STATUS_UPDATE_FAILED("Status updation failed"),
    ERROR_INCORRECT_PROFILE_ID("Incorrect Profile Id"),
    SUCCESS_KYC_APPLICATION_SAVED("KYC application saved successfully"),
    SUCCESS_KYC_IMAGES_SAVED("KYC images saved successfully"),
    SUCCESS_KYC_STATUS_UPDATED("KYC status updated successfully"),
	SUCCESS_KYC_APPLICATION_UPDATED("KYC Application updated successfully");
	

    // Path variables
    public static final String BASE_KYC_APPLICATION_URL = "/crime-reporting-system/kyc-application";
    public static final String GET_KYC_APPLICATION_BY_PROFILE_ID_PATH = "/getKycApplicationByProfileId/{profileId}";
    public static final String GET_ALL_KYC_APPLICATIONS_PATH = "/getAllKycApplications";
    public static final String SAVE_KYC_APPLICATION_IMAGES_PATH = "/saveKycApplicationImages/{kycApplicationId}";

    private final String value;

    KycApplicationConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
