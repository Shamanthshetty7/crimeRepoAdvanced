package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.CrimeReportingSystem.constants.ReportVoteConstants;
import com.endava.CrimeReportingSystem.entity.ReportVote;
import com.endava.CrimeReportingSystem.entity.Reports;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.ReportVoteDTO;
import com.endava.CrimeReportingSystem.entity.dto.ReportsDTO;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.mapper.ReportVoteMapper;
import com.endava.CrimeReportingSystem.mapper.ReportsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.ReportVoteRepository;
import com.endava.CrimeReportingSystem.repository.ReportsRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

@ExtendWith(MockitoExtension.class)
public class ReportVoteServiceTest {

    @Mock
    private ReportVoteRepository reportVoteRepository;

    @Mock
    private ReportsRepository reportsRepository;

    @Mock
    private ReportVoteMapper reportVoteMapper;

    @Mock
    private UsersMapper usersMapper;

    @Mock
    private ReportsMapper reportsMapper;

    @InjectMocks
    private ReportVoteServiceImpl reportVoteService;

    private ReportVote reportVote;
    private ReportVoteDTO reportVoteDTO;
    private Users user;
    private Reports report;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setUserId(1);
        user.setUserName("Shamanth");
        
        UsersDTO usersDTO=new UsersDTO();
        usersDTO.setUserId(1);
        usersDTO.setUserName("Shamanth");

        report = new Reports();
        report.setReportId(1);
        report.setReportTitle("Test Report");
        report.setReportUpvoteCount(0);
        report.setReportDownvoteCount(0);
        
        ReportsDTO reportsDTO = ReportsDTO.builder()
        	    .reportId(1)
        	    .reportTitle("Test Report")
        	    .reportUpvoteCount(0)
        	    .reportDownvoteCount(0)
        	    .build();

        
        reportVote = new ReportVote();
        reportVote.setReportVoteId(1);
        reportVote.setUser(user);
        reportVote.setReport(report);
        reportVote.setCreatedAt(LocalDateTime.now());
        reportVote.setUpVoted(false);
        reportVote.setDownVoted(false);

         reportVoteDTO = ReportVoteDTO.builder()
        	    .reportVoteId(1)
        	    .user(usersDTO)
        	    .report(reportsDTO)
        	    .build();

       
    }

    @Test
    void testGetReportVoteStatusByUserIdAndReportId_Success() {
        when(reportVoteRepository.findByUser_UserIdAndReport_ReportId(1, 1)).thenReturn(reportVote);
        when(reportVoteMapper.reportVoteToReportVoteDTO(reportVote)).thenReturn(reportVoteDTO);
       
        ApiGenericResponse<ReportVoteDTO> response = reportVoteService.getReportVoteStatusByUserIdAndReportId(1, 1);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().getReportVoteId());
        assertEquals("Shamanth", response.getData().getUser().getUserName());
        assertEquals("Test Report", response.getData().getReport().getReportTitle());
    }

    @Test
    void testGetReportVoteStatusByUserIdAndReportId_Failure() {
        when(reportVoteRepository.findByUser_UserIdAndReport_ReportId(anyInt(), anyInt())).thenReturn(null);

        ApiGenericResponse<ReportVoteDTO> response = reportVoteService.getReportVoteStatusByUserIdAndReportId(1, 1);

        assertNull(response.getData());
        assertEquals(ReportVoteConstants.ERROR_DOESNT_VOTED.getMessage(), response.getMessage());
    }

    @Test
    void testGetAllReportVote_Success() {
        List<ReportVote> reportVoteList = new ArrayList<>();
        reportVoteList.add(reportVote);

        when(reportVoteRepository.findAll()).thenReturn(reportVoteList);
        when(reportVoteMapper.reportVoteToReportVoteDTO(reportVote)).thenReturn(reportVoteDTO);
        when(usersMapper.usersToUsersDTO(user)).thenReturn(reportVoteDTO.getUser());
        when(reportsMapper.reportToReportDTO(report)).thenReturn(reportVoteDTO.getReport());

        List<ReportVoteDTO> response = reportVoteService.getAllReportVote();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1, response.get(0).getReportVoteId());
        assertEquals("Shamanth", response.get(0).getUser().getUserName());
        assertEquals("Test Report", response.get(0).getReport().getReportTitle());
    }

    @Test
    void testGetAllReportVote_EmptyList() {
        when(reportVoteRepository.findAll()).thenReturn(new ArrayList<>());

        List<ReportVoteDTO> response = reportVoteService.getAllReportVote();

        assertNotNull(response);
        assertEquals(0, response.size());
    }
    
    @Test
    void testUpdateReportVote_Success_Upvote() {
        when(reportVoteRepository.findById(anyInt())).thenReturn(Optional.of(reportVote));
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.of(report));

        ApiGenericResponse<?> response = reportVoteService.updateReportVote(1, "up");

        assertNotNull(response);
        assertEquals("Vote is updated!", response.getMessage());
        assertEquals(1, report.getReportUpvoteCount());
        assertEquals(0, report.getReportDownvoteCount());
    }

    @Test
    void testUpdateReportVote_Success_RemoveUpvote() {
        reportVote.setUpVoted(true);
        report.setReportUpvoteCount(1);

        when(reportVoteRepository.findById(anyInt())).thenReturn(Optional.of(reportVote));
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.of(report));

        ApiGenericResponse<?> response = reportVoteService.updateReportVote(1, "up");

        assertNotNull(response);
        assertEquals("Vote is been removed!", response.getMessage());
        assertEquals(0, report.getReportUpvoteCount());
    }

    @Test
    void testUpdateReportVote_Success_Downvote() {
        when(reportVoteRepository.findById(anyInt())).thenReturn(Optional.of(reportVote));
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.of(report));

        ApiGenericResponse<?> response = reportVoteService.updateReportVote(1, "down");

        assertNotNull(response);
        assertEquals("Vote is updated!", response.getMessage());
        assertEquals(0, report.getReportUpvoteCount());
        assertEquals(1, report.getReportDownvoteCount());
    }

    @Test
    void testUpdateReportVote_Success_RemoveDownvote() {
        reportVote.setDownVoted(true);
        report.setReportDownvoteCount(1);

        when(reportVoteRepository.findById(anyInt())).thenReturn(Optional.of(reportVote));
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.of(report));

        ApiGenericResponse<?> response = reportVoteService.updateReportVote(1, "down");

        assertNotNull(response);
        assertEquals("Vote is been removed!", response.getMessage());
        assertEquals(0, report.getReportDownvoteCount());
    }

    @Test
    void testUpdateReportVote_NoVoteAssociated() {
        when(reportVoteRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<?> response = reportVoteService.updateReportVote(1, "up");

        assertNull(response.getData());
        assertEquals("No votes associated with the given reportVoteId", response.getMessage());
    }

    @Test
    void testUpdateReportVote_ReportNotAvailable() {
        when(reportVoteRepository.findById(anyInt())).thenReturn(Optional.of(reportVote));
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<?> response = reportVoteService.updateReportVote(1, "up");

        assertNull(response.getData());
        assertEquals("Report is not available! maybe deleted from databse.", response.getMessage());
    }

    @Test
    void testUpdateReportVote_Success_UpvoteToDownvote() {
        // User already upvoted; transition to downvoted
        reportVote.setUpVoted(true);
        when(reportVoteRepository.findById(anyInt())).thenReturn(Optional.of(reportVote));
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.of(report));

        ApiGenericResponse<?> response = reportVoteService.updateReportVote(1, "down");

        assertNotNull(response);
        assertEquals("Vote is updated!", response.getMessage());
        assertEquals(false, reportVote.isUpVoted());
        assertEquals(true, reportVote.isDownVoted());
        assertEquals(-1, report.getReportUpvoteCount());
        assertEquals(1, report.getReportDownvoteCount());
    }
    
    @Test
    void testUpdateReportVote_Success_DownvoteToUpvote() {
       
        reportVote.setDownVoted(true);
        when(reportVoteRepository.findById(1)).thenReturn(Optional.of(reportVote));
        when(reportsRepository.findById(1)).thenReturn(Optional.of(report));

        ApiGenericResponse<?> response = reportVoteService.updateReportVote(1, "up");

       
        assertNotNull(response);
        assertEquals("Vote is updated!", response.getMessage());
        assertEquals(true, reportVote.isUpVoted());
        assertEquals(false, reportVote.isDownVoted());
        assertEquals(1, report.getReportUpvoteCount());
        assertEquals(-1, report.getReportDownvoteCount());
    }
}
