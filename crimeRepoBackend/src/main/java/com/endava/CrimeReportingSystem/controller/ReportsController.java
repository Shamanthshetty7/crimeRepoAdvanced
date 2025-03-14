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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.ReportsConstants;
import com.endava.CrimeReportingSystem.entity.dto.ReportsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.ReportsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ReportsConstants.BASE_REPORTS_URL)
@Slf4j  // Logger created automatically
public class ReportsController {

	private final ReportsServiceImpl reportsServiceImpl;

    // Constructor injection
    public ReportsController(ReportsServiceImpl reportsServiceImpl) {
        this.reportsServiceImpl = reportsServiceImpl;
    }

    
    /**
     * End point to fetch all reports.
     * @return A list of all reports or an error message if reports are not available
     */
    @GetMapping(path = ReportsConstants.GET_ALL_REPORTS_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getAllReports() {
        log.debug("ReportsController::getAllReports() - Fetching all reports.");

        try {
            List<ReportsDTO> reports = reportsServiceImpl.getAllReports();
            if (!reports.isEmpty()) {
                log.debug("ReportsController::getAllReports() - Successfully fetched all reports.");
                return new ResponseEntity<>(reports, HttpStatus.OK);
            } else {
                log.debug("ReportsController::getAllReports() - No reports available.");
                return new ResponseEntity<>("Reports not available", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("ReportsController::getAllReports() - Error occurred while fetching all reports. Error: {}", e.getMessage());
            return new ResponseEntity<>(ReportsConstants.ERROR_FETCHING_REPORTS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to fetch a specific report by its ID.
     * @param reportId The ID of the report to fetch
     * @return The report with the specified ID or an error message if not found
     */
    @GetMapping(path = ReportsConstants.GET_OR_DELETE_REPORT_BY_ID_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getReportById(@PathVariable int reportId) {
        log.debug("ReportsController::getReportById() - Fetching report with ID: {}", reportId);

        try {
            ApiGenericResponse<ReportsDTO> report = reportsServiceImpl.getReportById(reportId);
            if (report.getData() != null) {
                log.debug("ReportsController::getReportById() - Successfully fetched report with ID: {}", reportId);
                return new ResponseEntity<>(report.getData(), HttpStatus.OK);
            } else {
                log.debug("ReportsController::getReportById() - No report found with ID: {}", reportId);
                return new ResponseEntity<>(report.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("ReportsController::getReportById() - Error occurred while fetching report with ID: {}. Error: {}", reportId, e.getMessage());
            return new ResponseEntity<>(ReportsConstants.ERROR_FETCHING_REPORTS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to save a new report.
     * @param reportsDTO The report data to be saved
     * @return The saved report or an error message if the saving failed
     */
    @PostMapping(consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveReport(@RequestBody ReportsDTO reportsDTO) {
        log.debug("ReportsController::saveReport() - Saving report: {}", reportsDTO);

        try {
            ApiGenericResponse<ReportsDTO> addedReport = reportsServiceImpl.saveReport(reportsDTO);
            if (addedReport.getData() != null) {
                log.debug("ReportsController::saveReport() - Successfully saved report: {}", reportsDTO);
                return new ResponseEntity<>(addedReport.getData(), HttpStatus.OK);
            } else {
                log.debug("ReportsController::saveReport() - Failed to save report: {}", reportsDTO);
                return new ResponseEntity<>(addedReport.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("ReportsController::saveReport() - Error occurred while saving report. Error: {}", e.getMessage());
            return new ResponseEntity<>(ReportsConstants.ERROR_SAVING_REPORT.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * End point to save an image for a specific report.
     * @param reportId The ID of the report
     * @param reportImage The image file to be saved
     * @return The updated report with the image or an error message if saving fails
     */
    @PostMapping(path = ReportsConstants.SAVE_REPORT_IMAGE_PATH, consumes = {CommonConstants.MULTIPART_FILE}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveReportImage(@PathVariable int reportId, @RequestPart("reportImage") MultipartFile reportImage) throws Exception {
        log.debug("ReportsController::saveReportImage() - Saving image for reportId: {}", reportId);

        try {
            ApiGenericResponse<ReportsDTO> addedReportImage = reportsServiceImpl.saveReportImage(reportId, reportImage);
            if (addedReportImage.getData() != null) {
                log.debug("ReportsController::saveReportImage() - Successfully saved image for reportId: {}", reportId);
                return new ResponseEntity<>(addedReportImage.getData(), HttpStatus.OK);
            } else {
                log.debug("ReportsController::saveReportImage() - Failed to save image for reportId: {}", reportId);
                return new ResponseEntity<>(addedReportImage.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("ReportsController::saveReportImage() - Error occurred while saving image for reportId: {}. Error: {}", reportId, e.getMessage());
            return new ResponseEntity<>(ReportsConstants.ERROR_SAVING_REPORT_IMAGE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to update an existing report.
     * @param reportsDTO The report data to be updated
     * @return The updated report or an error message if the update failed
     */
    @PutMapping(consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> updateReport(@RequestBody ReportsDTO reportsDTO) {
        log.debug("ReportsController::updateReport() - Updating report: {}", reportsDTO);

        try {
            ApiGenericResponse<ReportsDTO> updatedReport = reportsServiceImpl.updateReport(reportsDTO);
            if (updatedReport.getData() != null) {
                log.debug("ReportsController::updateReport() - Successfully updated report: {}", reportsDTO);
                return new ResponseEntity<>(updatedReport.getData(), HttpStatus.OK);
            } else {
                log.debug("ReportsController::updateReport() - Failed to update report: {}", reportsDTO);
                return new ResponseEntity<>(updatedReport.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("ReportsController::updateReport() - Error occurred while updating report. Error: {}", e.getMessage());
            return new ResponseEntity<>(ReportsConstants.ERROR_UPDATING_REPORT.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to update the vote for a specific report.
     * @param reportId The ID of the report
     * @param reportsDTO The report data with the updated vote
     * @return The updated report with the new vote or an error message if the update failed
     */
    @PutMapping(path = ReportsConstants.UPDATE_REPORT_VOTE_PATH, consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> updateReportVote(@PathVariable int reportId, @RequestBody ReportsDTO reportsDTO) {
        log.debug("ReportsController::updateReportVote() - Updating vote for reportId: {}", reportId);

        try {
            ApiGenericResponse<ReportsDTO> updatedReportVote = reportsServiceImpl.updateReportVote(reportId, reportsDTO);
            if (updatedReportVote.getData() != null) {
                log.debug("ReportsController::updateReportVote() - Successfully updated vote for reportId: {}", reportId);
                return new ResponseEntity<>(updatedReportVote.getData(), HttpStatus.OK);
            } else {
                log.debug("ReportsController::updateReportVote() - Failed to update vote for reportId: {}", reportId);
                return new ResponseEntity<>(updatedReportVote.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("ReportsController::updateReportVote() - Error occurred while updating vote for reportId: {}. Error: {}", reportId, e.getMessage());
            return new ResponseEntity<>(ReportsConstants.ERROR_UPDATING_REPORT_VOTE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to delete a report by its ID.
     * @param reportId The ID of the report to delete
     * @return A success or failure response based on the deletion status
     */
    @DeleteMapping(ReportsConstants.GET_OR_DELETE_REPORT_BY_ID_PATH)
    public ResponseEntity<?> deleteReport(@PathVariable int reportId) {
        log.debug("ReportsController::deleteReport() - Deleting report with reportId: {}", reportId);

        try {
            boolean deleted = reportsServiceImpl.deleteReport(reportId);
            if (deleted) {
                log.debug("ReportsController::deleteReport() - Successfully deleted report with reportId: {}", reportId);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                log.debug("ReportsController::deleteReport() - Failed to delete report with reportId: {}", reportId);
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("ReportsController::deleteReport() - Error occurred while deleting report with reportId: {}. Error: {}", reportId, e.getMessage());
            return new ResponseEntity<>(ReportsConstants.ERROR_DELETING_REPORT.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}
