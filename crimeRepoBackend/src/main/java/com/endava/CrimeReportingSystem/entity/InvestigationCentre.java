package com.endava.CrimeReportingSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class InvestigationCentre {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int investigationCentreId;
	
	String investigationCentreName;
	String investigationCentreCode;
	LocalDateTime createdAt;
	

}
