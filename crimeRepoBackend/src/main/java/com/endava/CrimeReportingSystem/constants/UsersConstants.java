package com.endava.CrimeReportingSystem.constants;

public enum UsersConstants {

    // Generic messages
    ERROR_FETCHING_USERS("An unexpected error occurred while fetching users."),
    ERROR_FETCHING_USER("An unexpected error occurred while fetching user."),
    ERROR_CHECKING_USER_LOGIN(" An unexpected error occurred while checking user login."),
    ERROR_NO_USERS_FOUND("No users found"),
    ERROR_SAVING_USER("An unexpected error occurred while saving user."),
    ERROR_UPDATING_USER("An unexpected error occurred while updating user."),
    ERROR_DELETING_USER("An unexpected error occurred while deleting user."),
    ERROR_USER_NOT_FOUND("User not found"),
    ERROR_USER_LOGIN_FAILED("User login failed"),
    SUCCESS_USER_SAVED("User saved successfully"),
    SUCCESS_USER_UPDATED("User updated successfully"),
    SUCCESS_USER_DELETED("User deleted successfully"),
    SUCCESS_USER_LOGIN("User login successful"),
	
	MESSAGE_ACCOUNT_BLOCKED("Your account is blocked !You cant log in."),
	MESSAGE_DUPLICATE_EMAIL("Duplicate Email or Phone number"),
	MESSAGE_WRONG_INV_CODE("Wrong Investigation centre code!"),
	MESSAGE_NO_ACCOUNT_ASSOCIATED("Entered email doesn't have an account!"),
	MESSAGE_WRONG_PASSWORD("Wrong password!"),
	MESSAGE_INV_ACCOUNT("Please switch to Investigation login!Acoount is associated with Investigator."),
	MESSAGE_INF_ACCOUNT("Please switch to Informant login!Acoount is associated with Informant."),
	MESSAGE_WRONG_PASS_OR_INV_CODE("Wrong Password/investigation centre code!");

    // Path variables
    public static final String BASE_USER_URL = "/crime-reporting-system/users";
    public static final String GET_ALL_USERS_PATH = "/getAllUsers";
    public static final String GET_DELETE_USER_BY_ID_PATH = "/{userId}";
    public static final String USER_LOGIN_PATH = "/userLogin";
    public static final String USER_REGISTER_PATH = "/userRegister";
    public static final String USER_UPDATE_PATH = "/updateUser";

    private final String value;

    UsersConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
