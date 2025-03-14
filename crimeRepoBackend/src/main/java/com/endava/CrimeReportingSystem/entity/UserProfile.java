package com.endava.CrimeReportingSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int profileId;
	
	String userAddress;
	
	int userAge;
	String userGender;
	String userAlternativeNumber;
	Boolean isActive;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	
	@Lob
    private byte[] userProfileImage;
	
	@OneToOne
	@JoinColumn(name="userId")
	private Users users;
	
	
	
}
