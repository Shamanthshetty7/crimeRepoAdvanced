package com.endava.CrimeReportingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.endava.CrimeReportingSystem.entity.InvestigationCentre;


public interface InvestigationCentreRepository extends JpaRepository<InvestigationCentre, Integer> {

	public InvestigationCentre getByInvestigationCentreCode(String investigationCentreCode);
	
}
