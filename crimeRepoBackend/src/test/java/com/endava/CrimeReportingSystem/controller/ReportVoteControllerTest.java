package com.endava.CrimeReportingSystem.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.endava.CrimeReportingSystem.entity.dto.ReportVoteDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.ReportVoteServiceImpl;

@ExtendWith(MockitoExtension.class)
class ReportVoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ReportVoteServiceImpl reportVoteServiceImpl;

    @InjectMocks
    private ReportVoteController reportVoteController;

    private ReportVoteDTO reportVoteDTO;

    @BeforeEach
    void setUp() {
    	 reportVoteDTO = ReportVoteDTO.builder()
    		    .reportVoteId(1)
    		    .upVoted(true)
    		    .build();


        mockMvc = MockMvcBuilders.standaloneSetup(reportVoteController).build();
    }

    // Test for getReportVoteStatusByUserIdAndReportId
    @Test
    void testGetReportVoteStatusByUserIdAndReportId_Success() throws Exception {
    	ApiGenericResponse<ReportVoteDTO> response = new ApiGenericResponse<>(null, null);
     	response.setData(reportVoteDTO);
        when(reportVoteServiceImpl.getReportVoteStatusByUserIdAndReportId(1, 1))
                .thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/reportVote")
                .param("userId", "1")
                .param("reportId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportVoteId").value(1))
                .andExpect(jsonPath("$.upVoted").value(true));
    }

    @Test
    void testGetReportVoteStatusByUserIdAndReportId_NotFound() throws Exception {
    	
    	ApiGenericResponse<ReportVoteDTO> response = new ApiGenericResponse<>(null, null);
     	response.setMessage("No vote status found");
     	
        when(reportVoteServiceImpl.getReportVoteStatusByUserIdAndReportId(anyInt(), anyInt()))
                .thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/reportVote")
                .param("userId", "999")
                .param("reportId", "999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No vote status found"));
    }

    @Test
    void testGetReportVoteStatusByUserIdAndReportId_InternalServerError() throws Exception {
        when(reportVoteServiceImpl.getReportVoteStatusByUserIdAndReportId(anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/crime-reporting-system/reportVote")
                .param("userId", "1")
                .param("reportId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while fetching the vote status."));
    }

    // Test for getAllReports
    @Test
    void testGetAllReports_Success() throws Exception {
        List<ReportVoteDTO> reportVotes = new ArrayList<>();
        reportVotes.add(reportVoteDTO);
        when(reportVoteServiceImpl.getAllReportVote()).thenReturn(reportVotes);

        mockMvc.perform(get("/crime-reporting-system/reportVote/getAllReportVotes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].reportVoteId").value(1));
    }

    @Test
    void testGetAllReports_NotFound() throws Exception {
        when(reportVoteServiceImpl.getAllReportVote()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/crime-reporting-system/reportVote/getAllReportVotes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Report votes not available"));
    }

    @Test
    void testGetAllReports_InternalServerError() throws Exception {
        when(reportVoteServiceImpl.getAllReportVote()).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/crime-reporting-system/reportVote/getAllReportVotes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while fetching report votes."));
    }

    // Test for updateReportVote
    @Test
    void testUpdateReportVote_Success() throws Exception {
    	ApiGenericResponse<ReportVoteDTO> response = new ApiGenericResponse<>(null, null);
     	response.setData(reportVoteDTO);
     	response.setMessage( "Vote updated successfully");
        when(reportVoteServiceImpl.updateReportVote(anyInt(), anyString()))
                .thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/reportVote")
                .param("reportVoteId", "1")
                .param("voteStatus", "up")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Vote updated successfully"));
    }

    @Test
    void testUpdateReportVote_InternalServerError() throws Exception {
        when(reportVoteServiceImpl.updateReportVote(anyInt(), anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(put("/crime-reporting-system/reportVote")
                .param("reportVoteId", "1")
                .param("voteStatus", "up")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while updating the vote status."));
    }
    
    
}
