package com.endava.CrimeReportingSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.EmergencyConstants;
import com.endava.CrimeReportingSystem.entity.dto.EmergencyDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.EmergencyServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(EmergencyConstants.EMERGENCY_BASE_URL)
@Slf4j  // Create logger automatically
public class EmergencyController {

	private final EmergencyServiceImpl emergencyServiceImpl;

    // Constructor injection
    public EmergencyController(EmergencyServiceImpl emergencyServiceImpl) {
        this.emergencyServiceImpl = emergencyServiceImpl;
    }

    /**
     * Endpoint to add emergency number
     * @param emergencyDTO
     * @return added data or error message 
     */
    @PostMapping( consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> addEmergencyNumber(@RequestBody EmergencyDTO emergencyDTO) {
        log.debug("EmergencyController::addEmergencyNumber() - Entering method to add emergency number");

        try {
            ApiGenericResponse<EmergencyDTO> response = emergencyServiceImpl.addEmergencyNumber(emergencyDTO);
            if (response.getData() != null) {
                log.debug("EmergencyController::addEmergencyNumber() - Successfully added emergency number: {}", response.getData());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                log.debug("EmergencyController::addEmergencyNumber() - Failed to add emergency number");
                return new ResponseEntity<>(response.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("EmergencyController::addEmergencyNumber() - Error occurred while adding emergency number. Error: {}", e.getMessage());
            return new ResponseEntity<>(EmergencyConstants.ERROR_UNEXPECTED_ADDING.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to Fetch all emergency numbers
     * @return all the emergency numbers
     */
    @GetMapping( produces =  {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> fetchAllEmergencyNumbers() {
        log.debug("EmergencyController::fetchAllEmergencyNumbers() - Entering method to fetch all emergency numbers");

        try {
            List<EmergencyDTO> emergencyList = emergencyServiceImpl.fetchAllEmergencyNumbers();
            if (!emergencyList.isEmpty()) {
                log.debug("EmergencyController::fetchAllEmergencyNumbers() - Successfully retrieved emergency numbers: {}", emergencyList);
                return new ResponseEntity<>(emergencyList, HttpStatus.OK);
            } else {
                log.debug("EmergencyController::fetchAllEmergencyNumbers() - No emergency numbers found");
                return new ResponseEntity<>(EmergencyConstants.ERROR_EMERGENCY_NUMBERS_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("EmergencyController::fetchAllEmergencyNumbers() - Error occurred while fetching emergency numbers. Error: {}", e.getMessage());
            return new ResponseEntity<>(EmergencyConstants.ERROR_UNEXPECTED_FETCHING.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to update emergency number
     * @param emergencyDTO
     * @return updated emergency number or error message
     */
    @PutMapping( consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> updateEmergencyNumber(@RequestBody EmergencyDTO emergencyDTO) {
        log.debug("EmergencyController::updateEmergencyNumber() - Entering method to update emergency number");

        try {
            ApiGenericResponse<EmergencyDTO> response = emergencyServiceImpl.updateEmergencyNumber(emergencyDTO);
            if (response.getData() != null) {
                log.debug("EmergencyController::updateEmergencyNumber() - Successfully updated emergency number: {}", response.getData());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                log.debug("EmergencyController::updateEmergencyNumber() - Failed to update emergency number");
                return new ResponseEntity<>(response.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("EmergencyController::updateEmergencyNumber() - Error occurred while updating emergency number. Error: {}", e.getMessage());
            return new ResponseEntity<>(EmergencyConstants.ERROR_UNEXPECTED_UPDATING.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to delete emergency number
     * @param id
     * @return message about deletion
     */
    @DeleteMapping(path = EmergencyConstants.DELETE_EMERGENCY_NUMBER, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> deleteEmergencyNumber(@PathVariable int id) {
        log.debug("EmergencyController::deleteEmergencyNumber() - Entering method to delete emergency number with ID: {}", id);

        try {
            Boolean isDeleted = emergencyServiceImpl.deleteEmergencyNumber(id);
            if (isDeleted) {
                log.debug("EmergencyController::deleteEmergencyNumber() - Successfully deleted emergency number with ID: {}", id);
                return new ResponseEntity<>(EmergencyConstants.SUCCESS_EMERGENCY_NUMBER_DELETED.getMessage(), HttpStatus.OK);
            } else {
                log.debug("EmergencyController::deleteEmergencyNumber() - Failed to delete emergency number with ID: {}", id);
                return new ResponseEntity<>(EmergencyConstants.ERROR_FAILED_TO_DELETE.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("EmergencyController::deleteEmergencyNumber() - Error occurred while deleting emergency number with ID: {}. Error: {}", id, e.getMessage());
            return new ResponseEntity<>(EmergencyConstants.ERROR_UNEXPECTED_DELETING.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}