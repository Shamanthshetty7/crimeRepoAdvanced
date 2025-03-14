package com.endava.CrimeReportingSystem.mapper;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.Reports;
import com.endava.CrimeReportingSystem.entity.dto.ReportsDTO;

@Component
public class ReportsMapper {

	public ReportsDTO reportToReportDTO(Reports report) {
		return ReportsDTO.builder()
			    .createdAt(report.getCreatedAt())
			    .reportId(report.getReportId())
			    .reportTitle(report.getReportTitle())
			    .reportDescription(report.getReportDescription())
			    .reportLocation(report.getReportLocation())
			    .reportStatus(report.getReportStatus())
			    .reportDownvoteCount(report.getReportDownvoteCount())
			    .reportUpvoteCount(report.getReportUpvoteCount())
			    .detailedInformation(report.getDetailedInformation())
			    .isActive(report.getIsActive())
			    .build();

	}

	public Reports reportDtoToReport(ReportsDTO reportsDTO) {
		Reports report=new Reports();
		report.setCreatedAt(reportsDTO.getCreatedAt());
		report.setReportId(reportsDTO.getReportId());
		report.setReportTitle(reportsDTO.getReportTitle());
		report.setReportDescription(reportsDTO.getReportDescription());
		report.setReportLocation(reportsDTO.getReportLocation());
		report.setReportStatus(reportsDTO.getReportStatus());
		report.setReportDownvoteCount(reportsDTO.getReportDownvoteCount());
		report.setReportUpvoteCount(reportsDTO.getReportUpvoteCount());
		report.setDetailedInformation(reportsDTO.getDetailedInformation());
		report.setIsActive(reportsDTO.getIsActive());
		return report;
	}

	
}
