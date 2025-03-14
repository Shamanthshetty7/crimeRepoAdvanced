package com.endava.CrimeReportingSystem.constants;

public enum ReportsConstants {

    // Generic messages
    ERROR_FETCHING_REPORTS("An unexpected error occurred while fetching reports."),
    ERROR_NO_REPORTS_FOUND("Reports not available"),
    ERROR_SAVING_REPORT("An unexpected error occurred while saving the report."),
    ERROR_SAVING_REPORT_IMAGE("An unexpected error occurred while saving the report image."),
    ERROR_UPDATING_REPORT("An unexpected error occurred while updating the report."),
    ERROR_UPDATING_REPORT_VOTE("An unexpected error occurred while updating the vote for the report."),
    ERROR_DELETING_REPORT("An unexpected error occurred while deleting the report."),
    SUCCESS_REPORT_SAVED("Report saved successfully"),
    SUCCESS_REPORT_UPDATED("Report updated successfully"),
    SUCCESS_REPORT_IMAGE_SAVED("Report updated successfully with image"),
    SUCCESS_REPORT_DELETED("Report deleted successfully"),
    SUCCESS_REPORT_VOTE_UPDATED("Report vote updated successfully"),
	SUCCESS_NEW_REPORT_NEAR_YOUR_AREA("New Report Near Your Area!");

    // Path variables
    public static final String BASE_REPORTS_URL = "/crime-reporting-system/reports";
    public static final String GET_ALL_REPORTS_PATH = "/getAllReports";
    public static final String GET_OR_DELETE_REPORT_BY_ID_PATH = "/{reportId}";
    public static final String SAVE_REPORT_IMAGE_PATH = "/saveReportImage/{reportId}";
    public static final String UPDATE_REPORT_VOTE_PATH = "/vote/{reportId}";

    private final String value;

    ReportsConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
