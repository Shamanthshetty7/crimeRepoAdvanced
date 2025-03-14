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
public class Notifications {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int notificationId;
	
	String notificationTitle;
	String notificationMessage;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	Boolean isActive;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private Users user;
}
