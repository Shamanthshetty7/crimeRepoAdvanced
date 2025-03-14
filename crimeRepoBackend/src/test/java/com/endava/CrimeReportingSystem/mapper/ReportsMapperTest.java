package com.endava.CrimeReportingSystem.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.endava.CrimeReportingSystem.entity.Reports;
import com.endava.CrimeReportingSystem.entity.dto.ReportsDTO;
import com.endava.CrimeReportingSystem.enums.ReportStatus;

class ReportsMapperTest {

    private final ReportsMapper reportsMapper = new ReportsMapper();

    @Test
    void testReportToReportDTO_Success() {
        Reports report = new Reports();
        report.setCreatedAt(LocalDateTime.of(2023, 5, 1, 10, 30));
        report.setReportId(1);
        report.setReportTitle("Test Report");
        report.setReportDescription("This is a test report");
        report.setReportLocation("New York");
        report.setReportStatus(ReportStatus.newReport);
        report.setReportDownvoteCount(5);
        report.setReportUpvoteCount(20);
        report.setDetailedInformation("Additional details about the report");
        report.setIsActive(true);

      
        ReportsDTO reportsDTO = reportsMapper.reportToReportDTO(report);

        assertEquals(report.getCreatedAt(), reportsDTO.getCreatedAt());
        assertEquals(report.getReportId(), reportsDTO.getReportId());
        assertEquals(report.getReportTitle(), reportsDTO.getReportTitle());
        assertEquals(report.getReportDescription(), reportsDTO.getReportDescription());
        assertEquals(report.getReportLocation(), reportsDTO.getReportLocation());
        assertEquals(report.getReportStatus(), reportsDTO.getReportStatus());
        assertEquals(report.getReportDownvoteCount(), reportsDTO.getReportDownvoteCount());
        assertEquals(report.getReportUpvoteCount(), reportsDTO.getReportUpvoteCount());
        assertEquals(report.getDetailedInformation(), reportsDTO.getDetailedInformation());
        assertEquals(report.getIsActive(), reportsDTO.getIsActive());
    }

   

    @Test
    void testReportDtoToReport_Success() {
    	ReportsDTO reportsDTO = ReportsDTO.builder()
    		    .createdAt(LocalDateTime.of(2023, 5, 1, 10, 30))
    		    .reportId(1)
    		    .reportTitle("Test Report")
    		    .reportDescription("This is a test report")
    		    .reportLocation("New York")
    		    .reportStatus(ReportStatus.newReport)
    		    .reportDownvoteCount(5)
    		    .reportUpvoteCount(20)
    		    .detailedInformation("Additional details about the report")
    		    .isActive(true)
    		    .build();


       
        Reports report = reportsMapper.reportDtoToReport(reportsDTO);

        assertEquals(reportsDTO.getCreatedAt(), report.getCreatedAt());
        assertEquals(reportsDTO.getReportId(), report.getReportId());
        assertEquals(reportsDTO.getReportTitle(), report.getReportTitle());
        assertEquals(reportsDTO.getReportDescription(), report.getReportDescription());
        assertEquals(reportsDTO.getReportLocation(), report.getReportLocation());
        assertEquals(reportsDTO.getReportStatus(), report.getReportStatus());
        assertEquals(reportsDTO.getReportDownvoteCount(), report.getReportDownvoteCount());
        assertEquals(reportsDTO.getReportUpvoteCount(), report.getReportUpvoteCount());
        assertEquals(reportsDTO.getDetailedInformation(), report.getDetailedInformation());
        assertEquals(reportsDTO.getIsActive(), report.getIsActive());
    }

  
}
