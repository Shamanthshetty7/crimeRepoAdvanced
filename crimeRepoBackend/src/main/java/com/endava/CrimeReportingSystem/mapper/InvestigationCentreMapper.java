package com.endava.CrimeReportingSystem.mapper;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.InvestigationCentre;
import com.endava.CrimeReportingSystem.entity.dto.InvestigationCentreDTO;

@Component
public class InvestigationCentreMapper {

	
	public InvestigationCentreDTO investigationCentreToInvestigationCentreDTO(InvestigationCentre investigationCentre) {
		return new InvestigationCentreDTO(investigationCentre.getInvestigationCentreId(),investigationCentre.getInvestigationCentreCode(),investigationCentre.getInvestigationCentreName(),investigationCentre.getCreatedAt());
		
		
	}
	
	public InvestigationCentre InvestigationCentreDTOtoInvestigationCentre(InvestigationCentreDTO investigationCentreDTO) {
		InvestigationCentre investigationCentre=new InvestigationCentre();
		investigationCentre.setInvestigationCentreId(investigationCentreDTO.InvestigationCentreId());
		investigationCentre.setInvestigationCentreCode(investigationCentreDTO.InvestigationCentreCode());
		investigationCentre.setInvestigationCentreName(investigationCentreDTO.InvestigationCentreName());
		investigationCentre.setCreatedAt(investigationCentreDTO.createdAt());
		return investigationCentre;
	}
	
}
