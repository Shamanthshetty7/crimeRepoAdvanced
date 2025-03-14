package com.endava.CrimeReportingSystem.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ReportVoteDTO {

	int reportVoteId;
	ReportsDTO report;
	UsersDTO user;
	boolean upVoted;
	boolean downVoted;
	
}
