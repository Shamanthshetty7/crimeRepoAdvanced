package com.endava.CrimeReportingSystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.DashboardConstants;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.DashboardServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(DashboardConstants.DASHBOARD_BASE_URL)
@Slf4j  // Create logger automatically
public class DashboardController {
	
	private final DashboardServiceImpl dashboardServiceImpl;

    // Constructor injection
    public DashboardController(DashboardServiceImpl dashboardServiceImpl) {
        this.dashboardServiceImpl = dashboardServiceImpl;
    }

    /**
     * fetching all data counts such as total reports, total users etc
     * @return all the data counts
     */
    @GetMapping(path=DashboardConstants.GET_ALL_DATA_COUNTS, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getAllDataCount() {
        log.debug("DashboardController::getAllDataCount() - Entering method to fetch all data counts");

        try {
            ApiGenericResponse<?> alldataForDashboard = dashboardServiceImpl.getAllDatCount();
            if (alldataForDashboard.getData() != null) {
                log.debug("DashboardController::getAllDataCount() - Successfully retrieved dashboard data: {}", alldataForDashboard.getData());
                return new ResponseEntity<>(alldataForDashboard, HttpStatus.OK);
            } else {
                log.debug("DashboardController::getAllDataCount() - No data found for dashboard");
                return new ResponseEntity<>(DashboardConstants.ERROR_NO_DATA_FOUND.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("DashboardController::getAllDataCount() - Error occurred while fetching dashboard data. Error: {}", e.getMessage());
            return new ResponseEntity<>(DashboardConstants.ERROR_UNEXPECTED_FETCHING.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Fetching monthly report counts based on the year
     * @param year
     * @return monthly report counts 
     */
    @GetMapping(path=DashboardConstants.GET_REPORTS_BY_MONTH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getAllReportByMonth(@PathVariable int year){
        log.debug("DashboardController::getAllReportByMonth() - Entering method to fetch reports by month for year: {}", year);

        try {
            ApiGenericResponse<?> monthlyReportCounts = dashboardServiceImpl.getAllReportByMonth(year);

            if (monthlyReportCounts.getData() != null) {
                log.debug("DashboardController::getAllReportByMonth() - Successfully retrieved report counts for year {}: {}", year, monthlyReportCounts.getData());
                return new ResponseEntity<>(monthlyReportCounts.getData(), HttpStatus.OK);
            } else {
                log.debug("DashboardController::getAllReportByMonth() - No report data found for year: {}", year);
                return new ResponseEntity<>(monthlyReportCounts.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("DashboardController::getAllReportByMonth() - Error occurred while fetching report data for year {}. Error: {}", year, e.getMessage());
            return new ResponseEntity<>(DashboardConstants.ERROR_UNEXPECTED_FETCHING_REPORTS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
