package com.endava.CrimeReportingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.endava.CrimeReportingSystem.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer>{

	
	public UserProfile getByUsersUserId(int userId);
	
	
}
