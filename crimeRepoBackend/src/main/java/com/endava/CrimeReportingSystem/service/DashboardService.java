package com.endava.CrimeReportingSystem.service;

import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface DashboardService {
	
	public ApiGenericResponse<?> getAllDatCount(); 
	public ApiGenericResponse<?>  getAllReportByMonth(int year);
}
