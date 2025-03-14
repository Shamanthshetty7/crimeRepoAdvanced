package com.endava.CrimeReportingSystem.constants;

public enum ExternalApiConstants {

	
	//service
	ERROR_UNEXPECTED_FETCHING_INVESTIGATION("An unexpected error occurred while fetching investigation center data."),

	//controller
	
	// Error messages for unexpected scenarios
	ERROR_UNEXPECTED_FETCHING_COORDINATES("An unexpected error occurred while fetching coordinates."),
	ERROR_UNEXPECTED_FETCHING_NEARBY_SERVICES("An unexpected error occurred while fetching nearby emergency services."),
	ERROR_UNEXPECTED_VERIFYING_EMAIL("An unexpected error occurred while verifying email."),

	// Error messages for no data found
	ERROR_NO_DATA_FOUND_CITY("No data found for city"),
	ERROR_NO_COORDINATES_FOUND("No coordinates found for address"),
	ERROR_NO_NEARBY_SERVICES_FOUND("No nearby emergency services found for coordinates"),

	// Success messages
	SUCCESS_RETRIEVED_INVESTIGATION_DATA("Successfully retrieved investigation center data"),
	SUCCESS_RETRIEVED_COORDINATES("Successfully retrieved coordinates for address"),
	SUCCESS_RETRIEVED_NEARBY_SERVICES("Successfully retrieved nearby emergency services");

	// URL mappings
	public static final String BASE_API_URL = "/crime-reporting-system/APIcall";
	public static final String GET_INVESTIGATION_BY_CITY = "/{cityName}";
	public static final String POST_COORDINATES = "/coordinates";
	public static final String GET_NEARBY_EMERGENCY_SERVICES = "/emergency-services/{lat}/{lon}";
	public static final String GET_VERIFY_EMAIL = "/verifyEmail/{email}";
	
	public static final String POST_OFFICE_URL="https://api.postalpincode.in/postoffice/";
	public static final String OPENSTREETMAP_URL="https://nominatim.openstreetmap.org/";

	private final String value;

	ExternalApiConstants(String value) {
		this.value = value;
	}

	public String getMessage() {
		return value;
	}
}
