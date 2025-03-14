package com.endava.CrimeReportingSystem.constants;

public enum ReportVoteConstants {

    // Generic error messages
    ERROR_FETCHING_VOTE_STATUS("An unexpected error occurred while fetching the vote status."),
    ERROR_NO_VOTE_STATUS_FOUND("No vote status found for the provided userId and reportId"),
    ERROR_FETCHING_ALL_REPORT_VOTES("An unexpected error occurred while fetching report votes."),
    ERROR_UPDATING_VOTE_STATUS("An unexpected error occurred while updating the vote status."),
    ERROR_NO_REPORT_VOTES("Report votes not available"),
    ERROR_DOESNT_VOTED("Doesn't voted!"),
    ERROR_NO_VOTES_ASSOCIATED("No votes associated with the given reportVoteId"),
   ERROR_REPORT_NOT_AVAILABLE("Report is not available! maybe deleted from databse."),

    // Success messages
    SUCCESS_VOTE_STATUS_FETCHED("Successfully fetched vote status for the provided userId and reportId"),
    SUCCESS_ALL_REPORT_VOTES_FETCHED("Successfully fetched all report votes"),
    SUCCESS_VOTE_STATUS_UPDATED("Successfully updated vote status for the report"),
	SUCCESS_VOTE_REMOVED("Vote is been removed!"),
	SUCCESS_VOTE_UPDATED("Vote is updated!");

    // Paths
    public static final String BASE_REPORT_VOTE_URL = "/crime-reporting-system/reportVote";
    public static final String GET_ALL_REPORT_VOTES_PATH = "/getAllReportVotes";

    private final String value;

    ReportVoteConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
