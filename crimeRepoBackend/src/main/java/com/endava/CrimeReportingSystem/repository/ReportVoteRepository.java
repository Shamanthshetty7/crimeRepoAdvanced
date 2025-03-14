package com.endava.CrimeReportingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.endava.CrimeReportingSystem.entity.ReportVote;

public interface ReportVoteRepository extends JpaRepository<ReportVote, Integer> {

	
	 ReportVote findByUser_UserIdAndReport_ReportId(int user_UserId, int report_ReportId);
}
