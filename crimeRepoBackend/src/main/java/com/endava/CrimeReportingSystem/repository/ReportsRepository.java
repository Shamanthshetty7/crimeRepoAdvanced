package com.endava.CrimeReportingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.endava.CrimeReportingSystem.entity.Reports;

public interface ReportsRepository extends JpaRepository<Reports, Integer>{

	   
	    @Query("SELECT r.reportImage FROM Reports r WHERE r.reportId = :reportId")
	    byte[] findReportImageByReportId( int reportId);
	

	
}
