package com.endava.CrimeReportingSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Emergency {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int emergencyId;
	
	String emergencyContactType;
	String emergencyContactNumber;
	
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	@ManyToOne
	private Users user;
}
