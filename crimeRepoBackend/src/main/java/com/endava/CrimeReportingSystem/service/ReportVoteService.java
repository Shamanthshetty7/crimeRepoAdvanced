package com.endava.CrimeReportingSystem.service;

import java.util.List;

import com.endava.CrimeReportingSystem.entity.dto.ReportVoteDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface ReportVoteService {

	public ApiGenericResponse<ReportVoteDTO> getReportVoteStatusByUserIdAndReportId(int userId,int reportId);

	public List<ReportVoteDTO> getAllReportVote();

	public ApiGenericResponse<ReportVoteDTO> updateReportVote(int reportVoteId, String voteStatus);
	

	
}
