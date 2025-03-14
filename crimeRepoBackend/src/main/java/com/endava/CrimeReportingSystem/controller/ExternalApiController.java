package com.endava.CrimeReportingSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.ExternalApiConstants;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.utility.ExternalAPIs;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ExternalApiConstants.BASE_API_URL)
@Slf4j  // Create logger automatically
public class ExternalApiController {

	private final ExternalAPIs externalAPIs;

    // Constructor injection
    public ExternalApiController(ExternalAPIs externalAPIs) {
        this.externalAPIs = externalAPIs;
    }

    /**
     * End point to fetch investigation centers based on city name.
     * @param cityName The name of the city for which investigation centers are being queried
     * @return A list of matched cities or an error message if none found
     */
   
    @GetMapping(path=ExternalApiConstants.GET_INVESTIGATION_BY_CITY, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getInvestigationCentreById(@PathVariable  String cityName) {
        log.debug("ExternalApiController::getInvestigationCentreById() - Fetching investigation centre for city: {}", cityName);
        
        
            ApiGenericResponse<Object> cities = externalAPIs.getMatchedCitiesByCity(cityName);
            
            if (cities.getData() != null) {
                log.debug("ExternalApiController::getInvestigationCentreById() - Successfully retrieved data for city: {}", cityName);
                return new ResponseEntity<>(cities.getData(), HttpStatus.OK);
            } else {
                log.debug("ExternalApiController::getInvestigationCentreById() - No data found for city: {}", cityName);
                return new ResponseEntity<>(cities.getMessage(), HttpStatus.NOT_FOUND);
            }
        
    }

    /**
     * End point to fetch coordinates for a given address.
     * @param addresses, The list of address for which coordinates are being queried
     * @return Coordinates o all address or an error message if not found
     */
    @PostMapping(path = ExternalApiConstants.POST_COORDINATES, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getCoordinatesByAddress(@RequestBody List<String> addresses) {
        
        log.debug("ExternalApiController::getCoordinatesByAddress() - Fetching coordinates for address: {}", addresses);
        
        
            ApiGenericResponse<Object> coordinates = externalAPIs.getCoordinatesByAddress(addresses);
            
            if (coordinates.getData() != null) {
                log.debug("ExternalApiController::getCoordinatesByAddress() - Successfully retrieved coordinates for address: {}", addresses);
                return new ResponseEntity<>(coordinates.getData(), HttpStatus.OK);
            } else {
                log.debug("ExternalApiController::getCoordinatesByAddress() - No coordinates found for address: {}", addresses);
                return new ResponseEntity<>(coordinates.getMessage(), HttpStatus.NOT_FOUND);
            }
        
    }

    /**
     * End point to fetch nearby emergency services based on geographical coordinates (latitude, longitude).
     * @param latitude Latitude of the user's location
     * @param longitude Longitude of the user's location
     * @return Nearby emergency services or an error message if not found
     */
    
    @GetMapping(path =ExternalApiConstants.GET_NEARBY_EMERGENCY_SERVICES, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getNearbyEmergencyServices( @PathVariable("lat") double latitude,@PathVariable("lon") double longitude) {
        
        log.debug("ExternalApiController::getNearbyEmergencyServices() - Fetching nearby emergency services for coordinates: lat={}, lon={}", latitude, longitude);
       
            ApiGenericResponse<Object> nearbyServices = externalAPIs.getNearbyEmergencyServices(latitude, longitude);
            
            if (nearbyServices.getData() != null) {
                log.debug("ExternalApiController::getNearbyEmergencyServices() - Successfully retrieved nearby services for coordinates: lat={}, lon={}", latitude, longitude);
                return new ResponseEntity<>(nearbyServices.getData(), HttpStatus.OK);
            } else {
                log.debug("ExternalApiController::getNearbyEmergencyServices() - No nearby emergency services found for coordinates: lat={}, lon={}", latitude, longitude);
                return new ResponseEntity<>(nearbyServices.getMessage(), HttpStatus.NOT_FOUND);
            }
        
    }
    
    @GetMapping(path = ExternalApiConstants.GET_VERIFY_EMAIL, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> checkVerifiedEmail(@PathVariable String email) {
        log.debug("ExternalApiController::checkVerifiedEmail() - Verifying email: {}", email);

        
            ApiGenericResponse<?> response = externalAPIs.checkVerifiedEmail(email);

            if (response.getData() !=null) {
                log.debug("ExternalApiController::checkVerifiedEmail() - Successfully verified email: {}", email);
                return new ResponseEntity<>(response.getData(),HttpStatus.OK) ;
            } else {
            	log.error("ExternalApiController::checkVerifiedEmail() - Error occurred while verifying email: {}. Error: {}", email);
                return new ResponseEntity<>(ExternalApiConstants.ERROR_UNEXPECTED_VERIFYING_EMAIL.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        
    }
    
    
    

}
