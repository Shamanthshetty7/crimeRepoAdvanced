package com.endava.CrimeReportingSystem.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;

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
public class KycApplication {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int KycId;
	String userFullName;
	Date userDOB;
	
	@Lob
    private byte[] userVerificationImage; 
	
	@Lob
	private byte[] userAdhaarFile;
	
	private String adhaarFileType;
	
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	Boolean isActive;
	
	String currentCity;
	String fecthedUserLocation;
	
	@Enumerated(EnumType.STRING)
	private KycVerificationStatus kycVerificationStatus;  
	
	@ManyToOne
	@JoinColumn(name="userProfileId")
	private UserProfile userProfile;
}
