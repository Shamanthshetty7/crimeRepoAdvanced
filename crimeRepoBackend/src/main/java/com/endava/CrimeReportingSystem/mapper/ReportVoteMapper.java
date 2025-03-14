package com.endava.CrimeReportingSystem.mapper;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.ReportVote;
import com.endava.CrimeReportingSystem.entity.dto.ReportVoteDTO;

@Component
public class ReportVoteMapper {

    public ReportVoteDTO reportVoteToReportVoteDTO(ReportVote reportVote) {
    	return ReportVoteDTO.builder()
    		    .reportVoteId(reportVote.getReportVoteId())
    		    .upVoted(reportVote.isUpVoted())
    		    .downVoted(reportVote.isDownVoted())
    		    .build();

    }

    public ReportVote reportVoteDTOToReportVote(ReportVoteDTO reportVoteDTO) {
        ReportVote reportVote = new ReportVote();
        reportVote.setReportVoteId(reportVoteDTO.getReportVoteId());
        reportVote.setUpVoted(reportVoteDTO.isUpVoted());
        reportVote.setDownVoted(reportVoteDTO.isDownVoted());
        return reportVote;
    }
}