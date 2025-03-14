package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.endava.CrimeReportingSystem.constants.ReportsConstants;
import com.endava.CrimeReportingSystem.entity.ReportVote;
import com.endava.CrimeReportingSystem.entity.Reports;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.ReportsDTO;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.enums.ReportStatus;
import com.endava.CrimeReportingSystem.mapper.ReportsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.NotificationsRepository;
import com.endava.CrimeReportingSystem.repository.ReportVoteRepository;
import com.endava.CrimeReportingSystem.repository.ReportsRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

@ExtendWith(MockitoExtension.class)
class ReportsServiceTest {

    @Mock
    private ReportsRepository reportsRepository;
    
    @Mock
    private UsersRepository usersRepository;
    
    @Mock
    private ReportVoteRepository reportVoteRepository;
    
    @Mock
    private NotificationsRepository notificationsRepository;

    @Mock
    private ReportsMapper reportsMapper;

    @Mock
    private UsersMapper usersMapper;

    @InjectMocks
    private ReportsServiceImpl reportsService; 

    private Reports report;
    private ReportsDTO reportsDTO;
    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setUserId(1);
        user.setUserName("Shamanth");
        user.setUserLiveLocation("New York");
        
        UsersDTO usersDTO=new UsersDTO();
        usersDTO.setUserId(1);
        usersDTO.setUserName("Shamanth");
      


        report = new Reports();
        report.setReportId(1);
        report.setReportTitle("Test Report");
        report.setReportImage(new byte[]{1, 2, 3, 4});
        report.setUser(user);
        report.setReportUpvoteCount(10);
        report.setReportDownvoteCount(2);
        report.setReportLocation("New York");
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());
        report.setReportStatus(ReportStatus.newReport);

         reportsDTO = ReportsDTO.builder()
        	    .reportId(1)
        	    .reportTitle("Test Report")
        	    .reportImage("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(report.getReportImage()))
        	    .reportUpvoteCount(10)
        	    .reportDownvoteCount(2)
        	    .user(usersDTO)
        	    .reportLocation("New York")
        	    .createdAt(LocalDateTime.now())
        	    .reportStatus(ReportStatus.newReport)
        	    .build();

    }

    // Test for getReportById
    @Test
    void testGetReportById_Success() {
        when(reportsRepository.findById(anyInt())).thenReturn(java.util.Optional.of(report));
        when(reportsMapper.reportToReportDTO(report)).thenReturn(reportsDTO);
        when(usersMapper.usersToUsersDTO(user)).thenReturn(reportsDTO.getUser());

        ApiGenericResponse<ReportsDTO> response = reportsService.getReportById(1);

        assertNotNull(response.getData());
        assertEquals("Test Report", response.getData().getReportTitle());
        assertEquals("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(report.getReportImage()), response.getData().getReportImage());
        assertEquals(10, response.getData().getReportUpvoteCount());
        assertEquals(2, response.getData().getReportDownvoteCount());
    }

    @Test
    void testGetReportById_ReportNotFound() {
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<ReportsDTO> response = reportsService.getReportById(1);

        assertNull(response.getData());
        assertEquals(ReportsConstants.ERROR_NO_REPORTS_FOUND.getMessage(), response.getMessage());
    }

    // Test for getAllReports
    @Test
    void testGetAllReports_Success() {
        List<Reports> reportsList = new ArrayList<>();
        reportsList.add(report);

        when(reportsRepository.findAll()).thenReturn(reportsList);
        when(reportsMapper.reportToReportDTO(report)).thenReturn(reportsDTO);
        when(usersMapper.usersToUsersDTO(user)).thenReturn(reportsDTO.getUser());

        List<ReportsDTO> response = reportsService.getAllReports();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Test Report", response.get(0).getReportTitle());
        assertEquals(10, response.get(0).getReportUpvoteCount());
        assertEquals(2, response.get(0).getReportDownvoteCount());
    }

    @Test
    void testUpdateReport_ExistingImageRetrievedWhenImageIsNull() {
      
        when(reportsMapper.reportDtoToReport(any(ReportsDTO.class))).thenReturn(report);
        when(reportsRepository.findReportImageByReportId(anyInt())).thenReturn(new byte[]{1, 2, 3, 4});
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);
        when(reportsMapper.reportToReportDTO(any(Reports.class))).thenReturn(reportsDTO);

        reportsDTO.setReportImage(null);
        ApiGenericResponse<ReportsDTO> response = reportsService.updateReport(reportsDTO);

        assertNotNull(response.getData());
        assertEquals("Test Report", response.getData().getReportTitle());
        assertEquals(ReportsConstants.SUCCESS_REPORT_UPDATED.getMessage(), response.getMessage());
        verify(reportsRepository).findReportImageByReportId(reportsDTO.getReportId());
    }

    @Test
    void testGetAllReports_EmptyList() {
        when(reportsRepository.findAll()).thenReturn(new ArrayList<>());

        List<ReportsDTO> response = reportsService.getAllReports();

        assertNotNull(response);
        assertEquals(0, response.size());
    }
    
    @Test
    void testGetAllReports_SortingLogic() {
       
        Reports report1 = new Reports();
        report1.setReportId(1);
        report1.setReportTitle("Report 1");
        report1.setReportUpvoteCount(10);
        report1.setReportDownvoteCount(3);

        Reports report2 = new Reports();
        report2.setReportId(2);
        report2.setReportTitle("Report 2");
        report2.setReportUpvoteCount(5);
        report2.setReportDownvoteCount(2);

        Reports report3 = new Reports();
        report3.setReportId(3);
        report3.setReportTitle("Report 3");
        report3.setReportUpvoteCount(8);
        report3.setReportDownvoteCount(1);

        List<Reports> reportsList = List.of(report1, report2, report3);

        ReportsDTO dto1 = ReportsDTO.builder()
        	    .reportId(1)
        	    .reportTitle("Report 1")
        	    .reportUpvoteCount(10)
        	    .reportDownvoteCount(3)
        	    .build();

        	ReportsDTO dto2 = ReportsDTO.builder()
        	    .reportId(2)
        	    .reportTitle("Report 2")
        	    .reportUpvoteCount(5)
        	    .reportDownvoteCount(2)
        	    .build();

        	ReportsDTO dto3 = ReportsDTO.builder()
        	    .reportId(3)
        	    .reportTitle("Report 3")
        	    .reportUpvoteCount(8)
        	    .reportDownvoteCount(1)
        	    .build();


        when(reportsRepository.findAll()).thenReturn(reportsList);
        when(reportsMapper.reportToReportDTO(report1)).thenReturn(dto1);
        when(reportsMapper.reportToReportDTO(report2)).thenReturn(dto2);
        when(reportsMapper.reportToReportDTO(report3)).thenReturn(dto3);

        
        List<ReportsDTO> response = reportsService.getAllReports();

        assertNotNull(response);
        assertEquals(3, response.size());

        assertEquals("Report 1", response.get(0).getReportTitle());
        assertEquals(10 - 3, response.get(0).getReportUpvoteCount() - response.get(0).getReportDownvoteCount());

        assertEquals("Report 3", response.get(1).getReportTitle());
        assertEquals(8 - 1, response.get(1).getReportUpvoteCount() - response.get(1).getReportDownvoteCount());

        assertEquals("Report 2", response.get(2).getReportTitle());
        assertEquals(5 - 2, response.get(2).getReportUpvoteCount() - response.get(2).getReportDownvoteCount());
    }
    
 // Test for saveReport
    @Test
    void testSaveReport_Success() {
        List<Users> matchedUsers = new ArrayList<>();
        matchedUsers.add(user);

        when(reportsMapper.reportDtoToReport(any(ReportsDTO.class))).thenReturn(report);
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);
        when(usersRepository.findUsersByLiveLocationContainingIgnoreCase(any(), anyInt())).thenReturn(matchedUsers);
        when(reportsMapper.reportToReportDTO(any(Reports.class))).thenReturn(reportsDTO);
        when(notificationsRepository.saveAll(anyList())).thenReturn(anyList());
        ApiGenericResponse<ReportsDTO> response = reportsService.saveReport(reportsDTO);

        assertNotNull(response.getData());
        assertEquals("Test Report", response.getData().getReportTitle());
        assertEquals(ReportsConstants.SUCCESS_REPORT_SAVED.getMessage(), response.getMessage());
    }

    @Test
    void testSaveReport_Success_WithNotifications() {
       
        List<Users> matchedUsers = new ArrayList<>();
        matchedUsers.add(user);

        when(reportsMapper.reportDtoToReport(any(ReportsDTO.class))).thenReturn(report);
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);
        when(usersRepository.findUsersByLiveLocationContainingIgnoreCase(any(), anyInt())).thenReturn(matchedUsers);
        when(reportsMapper.reportToReportDTO(any(Reports.class))).thenReturn(reportsDTO);

     
        ApiGenericResponse<ReportsDTO> response = reportsService.saveReport(reportsDTO);

        assertNotNull(response.getData());
        assertEquals("Test Report", response.getData().getReportTitle());
        assertEquals(ReportsConstants.SUCCESS_REPORT_SAVED.getMessage(), response.getMessage());

        
    }


    // Test for saveReportImage
    @Test
    void testSaveReportImage_Success() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("reportImage", "image.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

        when(reportsRepository.findById(anyInt())).thenReturn(Optional.of(report));
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);
        when(reportsMapper.reportToReportDTO(any(Reports.class))).thenReturn(reportsDTO);

        ApiGenericResponse<ReportsDTO> response = reportsService.saveReportImage(1, imageFile);

        assertNotNull(response.getData());
        assertEquals("Report updated successfully with image", response.getMessage());
    }

    @Test
    void testSaveReportImage_ReportNotFound() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("reportImage", "image.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

        when(reportsRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<ReportsDTO> response = reportsService.saveReportImage(1, imageFile);

        assertNull(response.getData());
        assertEquals(ReportsConstants.ERROR_NO_REPORTS_FOUND.getMessage(), response.getMessage());
    }
    
    @Test
    void testUpdateReport_Success() {
        when(reportsMapper.reportDtoToReport(any(ReportsDTO.class))).thenReturn(report);
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);
        when(reportsMapper.reportToReportDTO(any(Reports.class))).thenReturn(reportsDTO);

        ApiGenericResponse<ReportsDTO> response = reportsService.updateReport(reportsDTO);

        assertNotNull(response.getData());
        assertEquals("Test Report", response.getData().getReportTitle());
        assertEquals(ReportsConstants.SUCCESS_REPORT_UPDATED.getMessage(), response.getMessage());
    }

    // Test for deleteReport
    @Test
    void testDeleteReport_Success() {
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.of(report));
        when(reportsRepository.save(report)).thenReturn(report);
        boolean isDeleted = reportsService.deleteReport(1);

        assertTrue(isDeleted);
        assertEquals(false, report.getIsActive());
        assertEquals(ReportStatus.removedReport, report.getReportStatus());
        verify(reportsRepository).save(any(Reports.class));
    }

    @Test
    void testDeleteReport_ReportNotFound() {
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.empty());
        boolean isDeleted = reportsService.deleteReport(1);

        assertEquals(false, isDeleted);
    }

    // Test for updateReportVote
    @Test
    void testUpdateReportVote_Upvote() {
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.of(report));
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);
        when(reportsMapper.reportToReportDTO(any(Reports.class))).thenReturn(reportsDTO);
        when(reportVoteRepository.save(any(ReportVote.class))).thenReturn(new ReportVote());

        reportsDTO.setVoteStatus("up");
        ApiGenericResponse<ReportsDTO> response = reportsService.updateReportVote(1, reportsDTO);

        assertNotNull(response.getData());
        assertEquals("Test Report", response.getData().getReportTitle());
        assertEquals(11, report.getReportUpvoteCount());
        assertEquals(2, report.getReportDownvoteCount());
    }

    @Test
    void testUpdateReportVote_Downvote() {
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.of(report));
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);
        when(reportsMapper.reportToReportDTO(any(Reports.class))).thenReturn(reportsDTO);
        when(reportVoteRepository.save(any(ReportVote.class))).thenReturn(new ReportVote());

        reportsDTO.setVoteStatus("down");
        ApiGenericResponse<ReportsDTO> response = reportsService.updateReportVote(1, reportsDTO);
         System.out.println(report);
        assertNotNull(response.getData());
        assertEquals("Test Report", response.getData().getReportTitle());
        assertEquals(10, report.getReportUpvoteCount());
        assertEquals(3, report.getReportDownvoteCount());
    }

    @Test
    void testUpdateReportVote_ReportNotFound() {
        when(reportsRepository.findById(anyInt())).thenReturn(Optional.empty());
        reportsDTO.setVoteStatus("up");

        ApiGenericResponse<ReportsDTO> response = reportsService.updateReportVote(1, reportsDTO);

        assertNull(response.getData());
        assertEquals("No report found with report id 1", response.getMessage());
    }

}
