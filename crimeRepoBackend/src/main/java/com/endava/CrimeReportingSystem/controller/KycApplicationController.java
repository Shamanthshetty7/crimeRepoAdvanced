package com.endava.CrimeReportingSystem.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.KycApplicationConstants;
import com.endava.CrimeReportingSystem.entity.dto.KycApplicationDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.KycApplicationServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(KycApplicationConstants.BASE_KYC_APPLICATION_URL)
@Slf4j  // Logger created automatically
public class KycApplicationController {

	private final KycApplicationServiceImpl kycApplicationServiceImpl;

    // Constructor injection

    public KycApplicationController(KycApplicationServiceImpl kycApplicationServiceImpl) {
        this.kycApplicationServiceImpl = kycApplicationServiceImpl;
    }

    
    /**
     * Endpoint to fetch a KYC application by a given profile ID.
     * @param profileId The profile ID associated with the KYC application
     * @return KYC application details or an error message if not found
     */
    @GetMapping(path=KycApplicationConstants.GET_KYC_APPLICATION_BY_PROFILE_ID_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getKycApplicationByProfileId(@PathVariable int profileId) {
        log.debug("KycApplicationController::getKycApplicationByProfileId() - Fetching KYC application for profileId: {}", profileId);

        try {
            ApiGenericResponse<KycApplicationDTO> kycApplications = kycApplicationServiceImpl.getKycApplicationByProfileId(profileId);

            if (kycApplications.getData() != null) {
                log.debug("KycApplicationController::getKycApplicationByProfileId() - Successfully retrieved KYC application for profileId: {}", profileId);
                return new ResponseEntity<>(kycApplications.getData(), HttpStatus.OK);
            } else {
                log.debug("KycApplicationController::getKycApplicationByProfileId() - No KYC application found for profileId: {}", profileId);
                return new ResponseEntity<>(KycApplicationConstants.ERROR_NO_KYC_APPLICATION_FOUND.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("KycApplicationController::getKycApplicationByProfileId() - Error occurred while fetching KYC application for profileId: {}. Error: {}", profileId, e.getMessage());
            return new ResponseEntity<>(KycApplicationConstants.ERROR_FETCHING_KYC_APPLICATION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to fetch all KYC applications.
     * @return List of all KYC applications or an error message if none are found
     */
    @GetMapping(path=KycApplicationConstants.GET_ALL_KYC_APPLICATIONS_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getAllKycApplications() {
        log.debug("KycApplicationController::getAllKycApplications() - Fetching all KYC applications.");

        try {
            List<KycApplicationDTO> kycApplications = kycApplicationServiceImpl.getAllKycApplications();

            if (!kycApplications.isEmpty()) {
                log.debug("KycApplicationController::getAllKycApplications() - Successfully retrieved {} KYC applications.", kycApplications.size());
                return new ResponseEntity<>(kycApplications, HttpStatus.OK);
            } else {
                log.debug("KycApplicationController::getAllKycApplications() - No KYC applications found.");
                return new ResponseEntity<>(KycApplicationConstants.ERROR_NO_KYC_APPLICATION_FOUND.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("KycApplicationController::getAllKycApplications() - Error occurred while fetching all KYC applications. Error: {}", e.getMessage());
            return new ResponseEntity<>(KycApplicationConstants.ERROR_FETCHING_ALL_KYC_APPLICATIONS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to save a new KYC application.
     * @param kycApplicationDTO The KYC application data to be saved
     * @return The saved KYC application or an error message
     */
    @PostMapping(consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveKycApplication(@RequestBody KycApplicationDTO kycApplicationDTO) {
        log.debug("KycApplicationController::saveKycApplication() - Saving new KYC application: {}", kycApplicationDTO);

        try {
            ApiGenericResponse<KycApplicationDTO> addedKycApplication = kycApplicationServiceImpl.saveKycApplicationData(kycApplicationDTO);

            if (addedKycApplication.getData() != null) {
                log.debug("KycApplicationController::saveKycApplication() - Successfully saved KYC application: {}", addedKycApplication.getData());
                return new ResponseEntity<>(addedKycApplication.getData(), HttpStatus.OK);
            } else {
                log.debug("KycApplicationController::saveKycApplication() - Failed to save KYC application: {}", addedKycApplication.getMessage());
                return new ResponseEntity<>(addedKycApplication.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("KycApplicationController::saveKycApplication() - Error occurred while saving KYC application. Error: {}", e.getMessage());
            return new ResponseEntity<>(KycApplicationConstants.ERROR_SAVING_KYC_APPLICATION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * End point to save KYC application images (user verification and Aadhaar file).
     * @param kycApplicationId The KYC application ID
     * @param kycImage The user verification image
     * @param adhaarFile The Aadhaar file
     * @return The updated KYC application or an error message
     */
    @PostMapping(path=KycApplicationConstants.SAVE_KYC_APPLICATION_IMAGES_PATH, consumes = {CommonConstants.MULTIPART_FILE}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveKycApplicationImages(@PathVariable int kycApplicationId, @RequestPart("userVerificationImage") MultipartFile kycImage, @RequestPart("userAdhaarFile") MultipartFile adhaarFile) throws IOException {
        log.debug("KycApplicationController::saveKycApplicationImages() - Saving KYC images for applicationId: {}", kycApplicationId);

        try {
            ApiGenericResponse<KycApplicationDTO> addedKycApplication = kycApplicationServiceImpl.saveKycApplicationImages(kycApplicationId, kycImage, adhaarFile);

            if (addedKycApplication.getData() != null) {
                log.debug("KycApplicationController::saveKycApplicationImages() - Successfully saved KYC images for applicationId: {}", kycApplicationId);
                return new ResponseEntity<>(addedKycApplication.getData(), HttpStatus.OK);
            } else {
                log.debug("KycApplicationController::saveKycApplicationImages() - Failed to save KYC images for applicationId: {}", kycApplicationId);
                return new ResponseEntity<>(addedKycApplication.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("KycApplicationController::saveKycApplicationImages() - Error occurred while saving KYC images for applicationId: {}. Error: {}", kycApplicationId, e.getMessage());
            return new ResponseEntity<>(KycApplicationConstants.ERROR_SAVING_KYC_IMAGES.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to update the status of a KYC application.
     * @param kycApplicationDTO The KYC application data containing the updated status
     * @return A success or failure message
     */
    @PutMapping(consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> updateKycStatus(@RequestBody KycApplicationDTO kycApplicationDTO) {
        log.debug("KycApplicationController::updateKycStatus() - Updating KYC status for KYC application ID: {}", kycApplicationDTO.getKycId());

        try {
            Boolean updatedKycStatus = kycApplicationServiceImpl.updateKycStatus(kycApplicationDTO.getKycId(), kycApplicationDTO.getKycVerificationStatus());

            if (updatedKycStatus) {
                log.debug("KycApplicationController::updateKycStatus() - Successfully updated KYC status for KYC application ID: {}", kycApplicationDTO.getKycId());
                return new ResponseEntity<>(KycApplicationConstants.SUCCESS_KYC_STATUS_UPDATED.getMessage(), HttpStatus.OK);
            } else {
                log.debug("KycApplicationController::updateKycStatus() - Failed to update KYC status for KYC application ID: {}", kycApplicationDTO.getKycId());
                return new ResponseEntity<>(KycApplicationConstants.ERROR_KYC_STATUS_UPDATE_FAILED.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("KycApplicationController::updateKycStatus() - Error occurred while updating KYC status for KYC application ID: {}. Error: {}", kycApplicationDTO.getKycId(), e.getMessage());
            return new ResponseEntity<>(KycApplicationConstants.ERROR_UPDATING_KYC_STATUS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
