package com.endava.CrimeReportingSystem.entity;

import java.time.LocalDateTime;

import com.endava.CrimeReportingSystem.enums.ReportStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Reports {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int reportId;
	
	String reportTitle;
	String reportDescription;
	int reportUpvoteCount;
	int reportDownvoteCount;
	String reportLocation;
	
	@Column(name = "detailed_information", columnDefinition = "TEXT")
	String detailedInformation;
	
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	
	@Lob
    private byte[] reportImage; 
	
	Boolean isActive;
	
	@Enumerated(EnumType.STRING)
	private ReportStatus reportStatus;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users user;
	
	
	 
}
