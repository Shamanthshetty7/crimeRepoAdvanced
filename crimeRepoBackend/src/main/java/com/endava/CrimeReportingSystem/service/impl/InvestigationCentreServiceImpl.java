package com.endava.CrimeReportingSystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.endava.CrimeReportingSystem.constants.InvestigationCentreConstants;
import com.endava.CrimeReportingSystem.entity.InvestigationCentre;
import com.endava.CrimeReportingSystem.entity.dto.InvestigationCentreDTO;
import com.endava.CrimeReportingSystem.mapper.InvestigationCentreMapper;
import com.endava.CrimeReportingSystem.repository.InvestigationCentreRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.InvestigationCentreService;

@Service
public class InvestigationCentreServiceImpl implements InvestigationCentreService {

	private final InvestigationCentreMapper investigationCentreMapper;
    private final InvestigationCentreRepository investigationCentreRepository;

    // Constructor injection
    public InvestigationCentreServiceImpl(InvestigationCentreMapper investigationCentreMapper,
                                          InvestigationCentreRepository investigationCentreRepository) {
        this.investigationCentreMapper = investigationCentreMapper;
        this.investigationCentreRepository = investigationCentreRepository;
    }

    /* Retrieves an investigation center by its unique code */
    @Override
    public ApiGenericResponse<InvestigationCentreDTO> getInvestigationCentreByInvCode(String investigationCentreCode) {
        InvestigationCentre investigationCentre = investigationCentreRepository.getByInvestigationCentreCode(investigationCentreCode);
        ApiGenericResponse<InvestigationCentreDTO> response = new ApiGenericResponse<>(null, null);
        
        if (investigationCentre == null) {
            response.setMessage(InvestigationCentreConstants.ERROR_NO_INVESTIGATION_CENTRE_FOUND.getMessage());
        } else {
            response.setData(investigationCentreMapper.investigationCentreToInvestigationCentreDTO(investigationCentre));
        }
        return response;
    }

    /* Retrieves all investigation centers from the database */
    @Override
    public List<InvestigationCentreDTO> getAllInvestigationCentre() {
        List<InvestigationCentre> allInvestigationCentres = investigationCentreRepository.findAll();
        
        // Converting the list of InvestigationCentre entities to InvestigationCentreDTO using stream
        return allInvestigationCentres.stream()
                .map(investigationCentreMapper::investigationCentreToInvestigationCentreDTO)
                .toList();
    }

    /* Adds a new investigation center to the system */
    @Override
    public ApiGenericResponse<InvestigationCentreDTO> saveInvestigationCentre(InvestigationCentreDTO investigationCentreDTO) {
       
        InvestigationCentre investigationCentre = investigationCentreMapper.InvestigationCentreDTOtoInvestigationCentre(investigationCentreDTO);
        investigationCentre.setCreatedAt(LocalDateTime.now());
        
        
        InvestigationCentre savedInvestigationCentre =Optional.ofNullable(investigationCentreRepository.save(investigationCentre))
        		.orElseThrow(() -> new RuntimeException(InvestigationCentreConstants.ERROR_SAVING_INVESTIGATION_CENTRE.getMessage()));;
        
        ApiGenericResponse<InvestigationCentreDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(investigationCentreMapper.investigationCentreToInvestigationCentreDTO(savedInvestigationCentre));
        response.setMessage(InvestigationCentreConstants.SUCCESS_INVESTIGATION_CENTRE_ADDED.getMessage());
        return response;
    }

    /* Updates the details of an existing investigation center */
    @Override
    public ApiGenericResponse<InvestigationCentreDTO> updateInvestigationCentre(InvestigationCentreDTO investigationCentreDTO) {
        InvestigationCentre savedInvestigationCentre = investigationCentreRepository.save(investigationCentreMapper.InvestigationCentreDTOtoInvestigationCentre(investigationCentreDTO));
        
        ApiGenericResponse<InvestigationCentreDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(investigationCentreMapper.investigationCentreToInvestigationCentreDTO(savedInvestigationCentre));
        response.setMessage(InvestigationCentreConstants.SUCCESS_INVESTIGATION_CENTRE_UPDATED.getMessage());
        return response;
    }

    /* Deletes an investigation center by its ID */
    @Override
    public Boolean deleteInvestigationCentre(int investigationCentreId) {
        // Searching for the investigation center by ID and deleting if found
    	Optional<InvestigationCentre> investigationCentre = investigationCentreRepository.findById(investigationCentreId);
        
        if (!investigationCentre.isEmpty()) {
            investigationCentreRepository.deleteById(investigationCentreId);
            return true; 
        }
        return false; 
    }
}
