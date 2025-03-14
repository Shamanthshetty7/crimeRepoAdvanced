package com.endava.CrimeReportingSystem.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.endava.CrimeReportingSystem.entity.ReportVote;
import com.endava.CrimeReportingSystem.entity.dto.ReportVoteDTO;

class ReportVoteMapperTest {

    private final ReportVoteMapper reportVoteMapper = new ReportVoteMapper();

    @Test
    void testReportVoteToReportVoteDTO_Success() {
       
        ReportVote reportVote = new ReportVote();
        reportVote.setReportVoteId(1);
        reportVote.setUpVoted(true);
        reportVote.setDownVoted(false);

        
        ReportVoteDTO reportVoteDTO = reportVoteMapper.reportVoteToReportVoteDTO(reportVote);

        assertEquals(reportVote.getReportVoteId(), reportVoteDTO.getReportVoteId());
        assertEquals(reportVote.isUpVoted(), reportVoteDTO.isUpVoted());
        assertEquals(reportVote.isDownVoted(), reportVoteDTO.isDownVoted());
    }

   
    @Test
    void testReportVoteDTOToReportVote_Success() {
        // Arrange: Create a test ReportVoteDTO
    	ReportVoteDTO reportVoteDTO = ReportVoteDTO.builder()
    		    .reportVoteId(1)
    		    .upVoted(false)
    		    .downVoted(true)
    		    .build();


       
        ReportVote reportVote = reportVoteMapper.reportVoteDTOToReportVote(reportVoteDTO);

     
        assertEquals(reportVoteDTO.getReportVoteId(), reportVote.getReportVoteId());
        assertEquals(reportVoteDTO.isUpVoted(), reportVote.isUpVoted());
        assertEquals(reportVoteDTO.isDownVoted(), reportVote.isDownVoted());
    }

   
}
