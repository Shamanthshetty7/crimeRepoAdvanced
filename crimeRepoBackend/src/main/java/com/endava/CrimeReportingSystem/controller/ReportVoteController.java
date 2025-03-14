package com.endava.CrimeReportingSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.ReportVoteConstants;
import com.endava.CrimeReportingSystem.entity.dto.ReportVoteDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.ReportVoteServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ReportVoteConstants.BASE_REPORT_VOTE_URL)
@Slf4j  // Logger created automatically
public class ReportVoteController {

	private final ReportVoteServiceImpl reportVoteServiceImpl;

    // Constructor injection
    public ReportVoteController(ReportVoteServiceImpl reportVoteServiceImpl) {
        this.reportVoteServiceImpl = reportVoteServiceImpl;
    }

    
    /**
     * End point to fetch the vote status for a specific user and report.
     * @param userId The ID of the user who voted
     * @param reportId The ID of the report that was voted on
     * @return The vote status or an error message if not found
     */
    @GetMapping(produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getReportVoteStatusByUserIdAndReportId(@RequestParam int userId, @RequestParam int reportId) {
        log.debug("ReportVoteController::getReportVoteStatusByUserIdAndReportId() - Fetching vote status for userId: {} and reportId: {}", userId, reportId);

        try {
            ApiGenericResponse<ReportVoteDTO> reportVoteIfExist = reportVoteServiceImpl.getReportVoteStatusByUserIdAndReportId(userId, reportId);
            if (reportVoteIfExist.getData() != null) {
                log.debug("ReportVoteController::getReportVoteStatusByUserIdAndReportId() - Found vote status for userId: {} and reportId: {}", userId, reportId);
                return new ResponseEntity<>(reportVoteIfExist.getData(), HttpStatus.OK);
            } else {
                log.debug("ReportVoteController::getReportVoteStatusByUserIdAndReportId() - No vote status found for userId: {} and reportId: {}", userId, reportId);
                return new ResponseEntity<>(reportVoteIfExist.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("ReportVoteController::getReportVoteStatusByUserIdAndReportId() - Error occurred while fetching vote status for userId: {} and reportId: {}. Error: {}", userId, reportId, e.getMessage());
            return new ResponseEntity<>(ReportVoteConstants.ERROR_FETCHING_VOTE_STATUS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    /**
     * End point to fetch all the votes on reports.
     * @return A list of all report votes or an error message if votes are not available
     */
    @GetMapping(path = ReportVoteConstants.GET_ALL_REPORT_VOTES_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getAllReports() {
        log.debug("ReportVoteController::getAllReports() - Fetching all report votes.");

        try {
            List<ReportVoteDTO> reportVotes = reportVoteServiceImpl.getAllReportVote();
            if (!reportVotes.isEmpty()) {
                log.debug("ReportVoteController::getAllReports() - Successfully fetched all report votes.");
                return new ResponseEntity<>(reportVotes, HttpStatus.OK);
            } else {
                log.debug("ReportVoteController::getAllReports() - No report votes available.");
                return new ResponseEntity<>(ReportVoteConstants.ERROR_NO_REPORT_VOTES.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("ReportVoteController::getAllReports() - Error occurred while fetching all report votes. Error: {}", e.getMessage());
            return new ResponseEntity<>(ReportVoteConstants.ERROR_FETCHING_ALL_REPORT_VOTES.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    /**
     * Endpoint to update the vote status for a specific report vote.
     * @param reportVoteId The ID of the report vote to be updated
     * @param voteStatus The new status for the vote (e.g., "upvoted" or "downvoted")
     * @return The updated vote status or an error message if the update failed
     */

    @PutMapping(produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> updateReportVote(@RequestParam int reportVoteId, @RequestParam String voteStatus) {
        log.debug("ReportVoteController::updateReportVote() - Updating vote status for reportVoteId: {} with voteStatus: {}", reportVoteId, voteStatus);

        try {
            ApiGenericResponse<ReportVoteDTO> updatedReportVote = reportVoteServiceImpl.updateReportVote(reportVoteId, voteStatus);
            log.debug("ReportVoteController::updateReportVote() - Successfully updated vote status for reportVoteId: {}", reportVoteId);
            return new ResponseEntity<>(updatedReportVote.getMessage(), HttpStatus.OK);
           
        } catch (Exception e) {
            log.error("ReportVoteController::updateReportVote() - Error occurred while updating vote status for reportVoteId: {}. Error: {}", reportVoteId, e.getMessage());
            return new ResponseEntity<>(ReportVoteConstants.ERROR_UPDATING_VOTE_STATUS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
