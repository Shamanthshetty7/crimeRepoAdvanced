package com.endava.CrimeReportingSystem.constants;

public enum DashboardConstants {

    // All messages for service impl
    ERROR_NO_REPORTS_AVAILABLE("No reports available for the selected year."),
    
    // Messages for controller responses
    ERROR_NO_DATA_FOUND("No data found for dashboard"),
    ERROR_REPORTS_NOT_AVAILABLE("Report data not available for the year"),
    ERROR_UNEXPECTED_FETCHING("An unexpected error occurred while fetching dashboard data."),
    ERROR_UNEXPECTED_FETCHING_REPORTS("An unexpected error occurred while fetching report data."),
    
    // Success messages
    SUCCESS_DASHBOARD_DATA_FETCHED("Dashboard data fetched successfully"),
    SUCCESS_REPORTS_FETCHED("Monthly report data fetched successfully");

    // Constants for URLs
    public static final String DASHBOARD_BASE_URL = "/crime-reporting-system/dashboard";
    public static final String GET_ALL_DATA_COUNTS = "/allDataCounts";
    public static final String GET_REPORTS_BY_MONTH = "/getReportsByMonth/{year}";

    private final String value;

    DashboardConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
