package com.endava.CrimeReportingSystem.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.entity.dto.ReportsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface ReportsService {

	public ApiGenericResponse<ReportsDTO> getReportById(int reportId);
	public List<ReportsDTO> getAllReports();
	public ApiGenericResponse<ReportsDTO> saveReport(ReportsDTO reportsDTO);
	public ApiGenericResponse<ReportsDTO> updateReport(ReportsDTO reportsDTO);
	public Boolean deleteReport(int reportId);
	public  ApiGenericResponse<ReportsDTO> saveReportImage(int reportId,MultipartFile reportImage) throws Exception;
	public ApiGenericResponse<ReportsDTO> updateReportVote(int reportId, ReportsDTO reportsDTO);
	}
