package com.endava.CrimeReportingSystem.constants;

public enum NewsConstants {

    // Generic messages
    ERROR_FETCHING_NEWS("An unexpected error occurred while fetching news."),
    ERROR_NO_NEWS_FOUND("News not found"),
    ERROR_SAVING_NEWS("An unexpected error occurred while saving news."),
    ERROR_SAVING_NEWS_IMAGE("An unexpected error occurred while saving news image."),
    ERROR_UPDATING_NEWS("An unexpected error occurred while updating news."),
    ERROR_DELETING_NEWS("An unexpected error occurred while deleting news."),
    ERROR_FETCHING_ALL_NEWS("An unexpected error occurred while fetching all news."),
    ERROR_NEWS_DELETION_FAILED("News deletion failed"),
    SUCCESS_NEWS_SAVED("News saved successfully"),
    SUCCESS_NEWS_IMAGE_SAVED("News image saved successfully"),
    SUCCESS_NEWS_UPDATED("News updated successfully"),
    SUCCESS_NEWS_DELETED("News deleted successfully");

    // Path variables
    public static final String BASE_NEWS_URL = "/crime-reporting-system/news";
    public static final String GET_NEWS_BY_ID_PATH = "/getNewsById/{newsId}";
    public static final String GET_ALL_NEWS_PATH = "/getAllNews";
    public static final String SAVE_NEWS_IMAGE_PATH = "/saveNewsImage/{newsId}";
    public static final String DELETE_BY_ID_PATH="/{newsId}";

    private final String value;

    NewsConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
