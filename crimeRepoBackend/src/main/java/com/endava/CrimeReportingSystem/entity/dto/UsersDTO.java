package com.endava.CrimeReportingSystem.entity.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.endava.CrimeReportingSystem.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDTO {

    int userId;
	
	String userName;
	String userEmail;
	String userPhoneNumber;
	String userPassword;
	String jwtToken;
	private UserType userType;
	LocalDateTime createdAt;
	String investigationCentreCode;
	Boolean isActive;
	String userLocationCoordinates;
	private List<ReportsDTO> reportsList;
}
