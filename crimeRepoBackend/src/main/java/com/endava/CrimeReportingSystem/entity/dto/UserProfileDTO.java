package com.endava.CrimeReportingSystem.entity.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserProfileDTO {

	int profileId;
	int userId;
    String userName;
	String userAddress;
	String userProfileImage;
	String userAlternativeNumber;
	int userAge;
	String userGender;
	
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	private UsersDTO user;
}
