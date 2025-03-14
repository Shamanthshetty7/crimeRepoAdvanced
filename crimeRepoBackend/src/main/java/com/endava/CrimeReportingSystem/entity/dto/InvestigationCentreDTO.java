package com.endava.CrimeReportingSystem.entity.dto;

import java.time.LocalDateTime;


public record InvestigationCentreDTO(
		int InvestigationCentreId,
		String InvestigationCentreName,
		String InvestigationCentreCode,
		LocalDateTime createdAt) {


}
