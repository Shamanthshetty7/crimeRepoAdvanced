package com.endava.CrimeReportingSystem.entity.dto;

import java.time.LocalDateTime;

public record EmergencyDTO(
    int emergencyId,
    String emergencyContactType,
    String emergencyContactNumber,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UsersDTO user
) {

	
	
}
