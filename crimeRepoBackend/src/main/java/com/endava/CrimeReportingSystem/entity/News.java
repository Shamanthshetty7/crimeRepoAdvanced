package com.endava.CrimeReportingSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int newsId;
	
	@Lob
	private byte[] newsImage;
	
	String newsHeadline;
	String newsSmallDescription;
	
	@Column(name = "news_details", columnDefinition = "TEXT")
	String newsDetails;
	
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	
	
	@ManyToOne
	@JoinColumn(name="userId")
	private Users user;
}
