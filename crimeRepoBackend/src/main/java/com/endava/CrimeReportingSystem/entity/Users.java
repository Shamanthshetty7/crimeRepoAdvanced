package com.endava.CrimeReportingSystem.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.endava.CrimeReportingSystem.enums.UserType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Users {
  
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int userId;
	
	String userName;
	String userEmail;
	String userPhoneNumber;
	String userPassword;
	Boolean isActive;
	String userLiveLocation;
	
	
	@Enumerated(EnumType.STRING)
	private UserType userType;
	
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	@ManyToOne
	@JoinColumn(name="investigationCentre_id")
	private InvestigationCentre investigationCentre;
	
	@OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
	private List<Reports> reports;
}
