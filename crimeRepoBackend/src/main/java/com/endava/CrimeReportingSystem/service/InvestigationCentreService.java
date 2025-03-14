package com.endava.CrimeReportingSystem.service;

import java.util.List;

import com.endava.CrimeReportingSystem.entity.dto.InvestigationCentreDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface InvestigationCentreService {

    public ApiGenericResponse<InvestigationCentreDTO> getInvestigationCentreByInvCode(String InvestigationCentreCode);
    public List<InvestigationCentreDTO> getAllInvestigationCentre();
    public ApiGenericResponse<InvestigationCentreDTO> saveInvestigationCentre(InvestigationCentreDTO investigationCentreDTO);
    public ApiGenericResponse<InvestigationCentreDTO> updateInvestigationCentre(InvestigationCentreDTO investigationCentreDTO);
    public Boolean deleteInvestigationCentre(int InvestigationCentreId);
}