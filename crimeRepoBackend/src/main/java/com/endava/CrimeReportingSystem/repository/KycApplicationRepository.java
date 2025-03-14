package com.endava.CrimeReportingSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.endava.CrimeReportingSystem.entity.KycApplication;

public interface KycApplicationRepository extends JpaRepository<KycApplication, Integer>{

	public Optional<KycApplication> getByUserProfileProfileIdAndIsActiveTrue(int profileId);
}
