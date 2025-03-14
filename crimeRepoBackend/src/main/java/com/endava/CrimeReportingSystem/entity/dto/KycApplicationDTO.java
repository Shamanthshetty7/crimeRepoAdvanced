package com.endava.CrimeReportingSystem.entity.dto;

import java.sql.Date;
import java.time.LocalDateTime;

import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KycApplicationDTO {
	int KycId;
	String userFullName;
	Date userDOB;
	
	String userVerificationImage; 
	String  userAdhaarFile;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	String currentCity;
	String fecthedUserLocation;
	private KycVerificationStatus kycVerificationStatus;  
	int userId;
	
	private UserProfileDTO userProfile;
	
}
