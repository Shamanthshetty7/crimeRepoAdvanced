package com.endava.CrimeReportingSystem.constants;

public enum UserProfileConstants {

    // Error messages
    ERROR_FETCHING_USER_PROFILE("An unexpected error occurred while fetching user profile."),
    ERROR_USER_PROFILE_NOT_FOUND("No user profile found for the provided ID"),
    ERROR_FETCHING_KYC_STATUS("An unexpected error occurred while fetching KYC status."),
    ERROR_KYC_STATUS_NOT_FOUND("No KYC status found for the provided user ID"),
    ERROR_SAVING_USER_PROFILE("An unexpected error occurred while saving user profile."),
    ERROR_UPDATING_USER_PROFILE("An unexpected error occurred while updating user profile."),
    ERROR_DELETING_USER_PROFILE("An unexpected error occurred while deleting user profile."),
    ERROR_SAVING_USER_PROFILE_IMAGE("An unexpected error occurred while saving user profile image."),
    ERROR_NO_USER_PROFILE_IMAGE("Failed to save user profile image. Please try again."),
    ERROR_PROFILE_UPDATE_FAILED("Failed to update user profile. Please try again."),
    ERROR_PROFILE_DELETE_FAILED("Failed to delete user profile. Please try again."),
    ERROR_NO_KYC_APPLICATION("no kyc application available"),

    // Success messages
    SUCCESS_USER_PROFILE_FETCHED("Successfully retrieved user profile."),
    SUCCESS_KYC_STATUS_FETCHED("Successfully retrieved KYC status for the user."),
    SUCCESS_USER_PROFILE_SAVED("Successfully saved user profile."),
    SUCCESS_USER_PROFILE_UPDATED("Successfully updated user profile."),
    SUCCESS_USER_PROFILE_DELETED("User profile deleted successfully."),
    SUCCESS_USER_PROFILE_IMAGE_SAVED("Successfully saved user profile image.");
    
    // Paths
	public static final String BASE_USER_PROFILE_URL="/crime-reporting-system/user-profile";
	public static final String GET_DELETE_USER_PROFILE_BY_ID_URL="/{userProfileId}";
	public static final String GET_KYC_STATUS_BY_USER_ID_URL="/KycStatus/{userId}";
	public static final String GET_USER_PROFILE_BY_USER_ID_URL="userProfile/{userId}";
	public static final String SAVE_USER_PROFILE_IMAGE_URL="/saveUserProfileImage/{userProfileId}";

    private final String value;

    UserProfileConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
