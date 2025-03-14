package com.endava.CrimeReportingSystem.entity.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewsDTO {

	
    int newsId;
	
	String newsImage;
	String newsHeadline;
	String newsSmallDescription;
	String newsDetails;
	
	
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	UsersDTO user;
}
