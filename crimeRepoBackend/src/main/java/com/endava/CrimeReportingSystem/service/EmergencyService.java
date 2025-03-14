package com.endava.CrimeReportingSystem.service;

import java.util.List;

import com.endava.CrimeReportingSystem.entity.dto.EmergencyDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface EmergencyService {
	ApiGenericResponse<EmergencyDTO> addEmergencyNumber(EmergencyDTO emergencyDTO);
	List<EmergencyDTO> fetchAllEmergencyNumbers();
	ApiGenericResponse<EmergencyDTO> updateEmergencyNumber(EmergencyDTO emergencyDTO);
	Boolean deleteEmergencyNumber(int emergencyId);
}
