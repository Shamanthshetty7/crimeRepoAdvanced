package com.endava.CrimeReportingSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ReportVote {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int reportVoteId;
	
	boolean upVoted;
	boolean downVoted;
	
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="reportId")
	private Reports report;
	
	
	
}
