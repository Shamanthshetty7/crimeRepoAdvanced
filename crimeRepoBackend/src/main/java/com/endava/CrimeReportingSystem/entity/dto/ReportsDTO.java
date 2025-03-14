package com.endava.CrimeReportingSystem.entity.dto;

import java.time.LocalDateTime;

import com.endava.CrimeReportingSystem.enums.ReportStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ReportsDTO {

    int reportId;
	
	String reportTitle;
	String reportDescription;
	int reportUpvoteCount;
	int reportDownvoteCount;
	String voteStatus;
	String reportLocation;
	LocalDateTime createdAt;
	String reportImage;
	String detailedInformation;
	Boolean isActive;
	
	private ReportStatus reportStatus;
	private UsersDTO user;
}
