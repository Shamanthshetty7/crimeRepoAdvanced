package com.endava.CrimeReportingSystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.endava.CrimeReportingSystem.constants.EmergencyConstants;
import com.endava.CrimeReportingSystem.entity.Emergency;
import com.endava.CrimeReportingSystem.entity.dto.EmergencyDTO;
import com.endava.CrimeReportingSystem.mapper.EmergencyMapper;
import com.endava.CrimeReportingSystem.repository.EmergencyRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.EmergencyService;

@Service
public class EmergencyServiceImpl implements EmergencyService {

	private final EmergencyRepository emergencyRepository;
    private final EmergencyMapper emergencyMapper;

    // Constructor injection
    public EmergencyServiceImpl(EmergencyRepository emergencyRepository,
                                EmergencyMapper emergencyMapper) {
        this.emergencyRepository = emergencyRepository;
        this.emergencyMapper = emergencyMapper;
    }

    /* Adds a new emergency number to the system */
    @Override
    public ApiGenericResponse<EmergencyDTO> addEmergencyNumber(EmergencyDTO emergencyDTO) {
		
    	ApiGenericResponse<EmergencyDTO> response = new ApiGenericResponse<>(null, null);
     
        Emergency emergency = emergencyMapper.emergencyDTOToEmergency(emergencyDTO);
        emergency.setCreatedAt(LocalDateTime.now());
        
 
        Emergency savedEmergency = Optional.ofNullable( emergencyRepository.save(emergency))
        		.orElseThrow(() -> new RuntimeException(EmergencyConstants.ERROR_FAILED_TO_ADD.getMessage()));
        
        EmergencyDTO savedEmergencyDTO = emergencyMapper.emergencyToEmergencyDTO(savedEmergency);
        response.setData(savedEmergencyDTO);
        response.setMessage(EmergencyConstants.SUCCESS_EMERGENCY_NUMBER_ADDED.getMessage());
        
       
        return response;
    }

    /* Fetches all emergency numbers from the database */
    @Override
    public List<EmergencyDTO> fetchAllEmergencyNumbers() {
        List<Emergency> emergencies = emergencyRepository.findAll();
        
        // Using stream to map list of Emergency entities to EmergencyDTOs
        return emergencies.stream()
                .map(emergencyMapper::emergencyToEmergencyDTO).toList();
               
    }

    /* Updates an existing emergency number in the system */
    @Override
    public ApiGenericResponse<EmergencyDTO> updateEmergencyNumber(EmergencyDTO emergencyDTO) {
    	ApiGenericResponse<EmergencyDTO> response = new ApiGenericResponse<>(null, null);
        
       
        Emergency emergency = emergencyMapper.emergencyDTOToEmergency(emergencyDTO);
        emergency.setUpdatedAt(LocalDateTime.now());
        
     try {
    	 
        Emergency updatedEmergency = emergencyRepository.save(emergency);
        EmergencyDTO updatedEmergencyDTO = emergencyMapper.emergencyToEmergencyDTO(updatedEmergency);
        response.setData(updatedEmergencyDTO);
        response.setMessage(EmergencyConstants.SUCCESS_EMERGENCY_NUMBER_UPDATED.getMessage());

     }catch (Exception e) {
         response.setMessage(EmergencyConstants.ERROR_FAILED_TO_UPDATE.getMessage());

	}
        
       
           
        
       
       return response;
    }

    /* Deletes an emergency number by its ID */
    @Override
    public Boolean deleteEmergencyNumber(int emergencyId) {
        try {
            emergencyRepository.deleteById(emergencyId);
            return true; // Return true if deletion is successful
        } catch (Exception e) {
            return false; // Return false if any exception occurs during deletion
        }
    }
}
