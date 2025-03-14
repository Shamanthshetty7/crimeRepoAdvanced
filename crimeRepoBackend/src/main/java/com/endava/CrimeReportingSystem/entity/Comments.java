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
public class Comments {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int commentId;
	
	String commentText;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private Users users;
	
	@ManyToOne
	@JoinColumn(name="reportId")
	private Reports reports;
	
	
}
