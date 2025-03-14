package com.endava.CrimeReportingSystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.endava.CrimeReportingSystem.constants.ReportVoteConstants;
import com.endava.CrimeReportingSystem.entity.ReportVote;
import com.endava.CrimeReportingSystem.entity.Reports;
import com.endava.CrimeReportingSystem.entity.dto.ReportVoteDTO;
import com.endava.CrimeReportingSystem.mapper.ReportVoteMapper;
import com.endava.CrimeReportingSystem.mapper.ReportsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.ReportVoteRepository;
import com.endava.CrimeReportingSystem.repository.ReportsRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.ReportVoteService;

@Service
public class ReportVoteServiceImpl implements ReportVoteService{

	private final ReportVoteRepository reportVoteRepository;
    private final ReportsRepository reportsRepository;
    private final ReportVoteMapper reportVoteMapper;
    private final UsersMapper usersMapper;
    private final ReportsMapper reportsMapper;

    // Constructor injection
    public ReportVoteServiceImpl(ReportVoteRepository reportVoteRepository,
                                 ReportsRepository reportsRepository,
                                 ReportVoteMapper reportVoteMapper,
                                 UsersMapper usersMapper,
                                 ReportsMapper reportsMapper) {
        this.reportVoteRepository = reportVoteRepository;
        this.reportsRepository = reportsRepository;
        this.reportVoteMapper = reportVoteMapper;
        this.usersMapper = usersMapper;
        this.reportsMapper = reportsMapper;
    }
	
	/**
	 *  Method to get the vote status for a specific user on a report
	 */
	@Override
	public ApiGenericResponse<ReportVoteDTO> getReportVoteStatusByUserIdAndReportId(int userId, int reportId) {
		ApiGenericResponse<ReportVoteDTO> response=new ApiGenericResponse<>(null,null);

		ReportVote existingReportVote=reportVoteRepository.findByUser_UserIdAndReport_ReportId(userId,reportId);
		if(existingReportVote==null) {
			response.setMessage(ReportVoteConstants.ERROR_DOESNT_VOTED.getMessage()); 
			return response;
		}
		response.setData(reportVoteMapper.reportVoteToReportVoteDTO(existingReportVote));
		return response;
	}
	
	/**
	 * Method to get all votes for reports
	 */
	@Override
	public List<ReportVoteDTO> getAllReportVote() {
		List<ReportVote> reportsVoteList = reportVoteRepository.findAll();
		
	   return reportsVoteList.stream().map((reportVote) -> {
	    	
	    	ReportVoteDTO reportVoteDTO =reportVoteMapper.reportVoteToReportVoteDTO(reportVote);
	      
	    	reportVoteDTO.setUser(usersMapper.usersToUsersDTO(reportVote.getUser()));
	    	reportVoteDTO.setReport(reportsMapper.reportToReportDTO(reportVote.getReport()));
	        return reportVoteDTO;
	    }).toList();
	    
	}
	
	
	/**
	 *  Method to update the vote status (upvote or downvote) for a report
	 */
	@Override
	public ApiGenericResponse<ReportVoteDTO> updateReportVote(int reportVoteId,String voteStatus) {
		ApiGenericResponse<ReportVoteDTO> response=new ApiGenericResponse<>(null,null);
		
		Optional<ReportVote> existingVote=reportVoteRepository.findById(reportVoteId);
		if (existingVote.isEmpty()) {
			response.setMessage(ReportVoteConstants.ERROR_NO_VOTES_ASSOCIATED.getMessage());	
			return response;
		}
		
		Optional<Reports> updatingReport=reportsRepository.findById(existingVote.get().getReport().getReportId());
		if(updatingReport.isEmpty()) {
			response.setMessage(ReportVoteConstants.ERROR_REPORT_NOT_AVAILABLE.getMessage());	
			return response;
		}
		
		// Handling updating cases
		switch (voteStatus) {
		    case "up" -> {
		        // If the user has already upvoted, remove the upvote and decrease the upvote count
		        if (existingVote.get().isUpVoted()) {
		            existingVote.get().setUpVoted(false);
		            updatingReport.get().setReportUpvoteCount(updatingReport.get().getReportUpvoteCount() - 1);
		            response.setMessage(ReportVoteConstants.SUCCESS_VOTE_REMOVED.getMessage());
		        } 
		        // If the user had previously downvoted, we update the vote: remove the downvote and add the upvote
		        else if (existingVote.get().isDownVoted()) {
		            existingVote.get().setUpVoted(true);
		            existingVote.get().setDownVoted(false);
		            updatingReport.get().setReportUpvoteCount(updatingReport.get().getReportUpvoteCount() + 1);
		            updatingReport.get().setReportDownvoteCount(updatingReport.get().getReportDownvoteCount() - 1);
		            response.setMessage(ReportVoteConstants.SUCCESS_VOTE_UPDATED.getMessage());
		        } 
		        // If the user hadn't voted yet, simply add the upvote
		        else {
		            existingVote.get().setUpVoted(true);
		            updatingReport.get().setReportUpvoteCount(updatingReport.get().getReportUpvoteCount() + 1);
		            response.setMessage(ReportVoteConstants.SUCCESS_VOTE_UPDATED.getMessage());
		        }
		    }
		    case "down" -> {
		        // If the user had already downvoted, remove the downvote and decrease the downvote count
		        if (existingVote.get().isDownVoted()) {
		            existingVote.get().setDownVoted(false);
		            updatingReport.get().setReportDownvoteCount(updatingReport.get().getReportDownvoteCount() - 1);
		            response.setMessage(ReportVoteConstants.SUCCESS_VOTE_REMOVED.getMessage());
		        } 
		        // If the user had previously upvoted, update the vote: remove the upvote and add the downvote
		        else if (existingVote.get().isUpVoted()) {
		            existingVote.get().setUpVoted(false);
		            existingVote.get().setDownVoted(true);
		            updatingReport.get().setReportUpvoteCount(updatingReport.get().getReportUpvoteCount() - 1);
		            updatingReport.get().setReportDownvoteCount(updatingReport.get().getReportDownvoteCount() + 1);
		            response.setMessage(ReportVoteConstants.SUCCESS_VOTE_UPDATED.getMessage());
		        } 
		        // If the user hadn't voted yet, simply add the downvote
		        else {
		            existingVote.get().setDownVoted(true);
		            updatingReport.get().setReportDownvoteCount(updatingReport.get().getReportDownvoteCount() + 1);
		            response.setMessage(ReportVoteConstants.SUCCESS_VOTE_UPDATED.getMessage());
		        }
		    }
		    default->response.setMessage(ReportVoteConstants.ERROR_UPDATING_VOTE_STATUS.getMessage());
		}

		existingVote.get().setUpdatedAt(LocalDateTime.now());
		reportsRepository.save(updatingReport.get());
		reportVoteRepository.save(existingVote.get());
		return response;
		
		
	}

}
