package com.endava.CrimeReportingSystem.entity.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class CommentsDTO {

    int commentId;
	
	String commentText;
	LocalDateTime createdAt;
	
	private UsersDTO users;
	private ReportsDTO reports;
}
