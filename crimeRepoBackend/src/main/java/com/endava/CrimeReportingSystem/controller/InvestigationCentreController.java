package com.endava.CrimeReportingSystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.InvestigationCentreConstants;
import com.endava.CrimeReportingSystem.entity.dto.InvestigationCentreDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.InvestigationCentreServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(InvestigationCentreConstants.BASE_INVESTIGATION_CENTRE_URL)
@Slf4j  // Logger created automatically
public class InvestigationCentreController {

	private final InvestigationCentreServiceImpl investigationCentreServiceImpl;

    // Constructor injection
    public InvestigationCentreController(InvestigationCentreServiceImpl investigationCentreServiceImpl) {
        this.investigationCentreServiceImpl = investigationCentreServiceImpl;
    }

    /**
     * End point to fetch details of an investigation center by its unique code.
     * @param investigationCentreCode The unique code of the investigation center
     * @return The details of the investigation center or an error message if not found
     */
    @GetMapping(path=InvestigationCentreConstants.INVESTIGATION_CENTRE_CODE_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getInvestigationCentreById(@PathVariable String investigationCentreCode) {
        log.debug("InvestigationCentreController::getInvestigationCentreById() - Fetching investigation centre details for code: {}", investigationCentreCode);

        try {
            ApiGenericResponse<InvestigationCentreDTO> invCentre = investigationCentreServiceImpl.getInvestigationCentreByInvCode(investigationCentreCode);

            if (invCentre.getData() != null) {
                log.debug("InvestigationCentreController::getInvestigationCentreById() - Successfully retrieved data for investigation centre code: {}", investigationCentreCode);
                return new ResponseEntity<>(invCentre.getData(), HttpStatus.OK);
            } else {
                log.debug("InvestigationCentreController::getInvestigationCentreById() - No data found for investigation centre code: {}", investigationCentreCode);
                return new ResponseEntity<>(invCentre.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("InvestigationCentreController::getInvestigationCentreById() - Error occurred while fetching investigation centre data for code: {}. Error: {}", investigationCentreCode, e.getMessage());
            return new ResponseEntity<>(InvestigationCentreConstants.ERROR_FETCHING_INVESTIGATION_CENTRE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
