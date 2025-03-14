package com.endava.CrimeReportingSystem.constants;

public enum InvestigationCentreConstants {

    // Generic messages
    ERROR_FETCHING_INVESTIGATION_CENTRE ("An unexpected error occurred while fetching data."),
    ERROR_NO_INVESTIGATION_CENTRE_FOUND ("Investigation Centre doesn't exist"),
    ERROR_SAVING_INVESTIGATION_CENTRE("Error saving investigation centre code"),
    
    SUCCESS_INVESTIGATION_CENTRE_ADDED("Investigation Centre Added Successfully"),
    SUCCESS_INVESTIGATION_CENTRE_UPDATED("Investigation Centre Updated Successfully");


    // Path variables
    public static final String BASE_INVESTIGATION_CENTRE_URL = "/crime-reporting-system/investigation-centre";
    public static final String INVESTIGATION_CENTRE_CODE_PATH = "/{investigationCentreCode}";

    private final String value;

    InvestigationCentreConstants(String value) {
		this.value = value;
	}

	public String getMessage() {
		return value;
	}
}
