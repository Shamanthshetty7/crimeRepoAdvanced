package com.endava.CrimeReportingSystem.service.impl;

import java.time.Month;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.endava.CrimeReportingSystem.constants.DashboardConstants;
import com.endava.CrimeReportingSystem.entity.KycApplication;
import com.endava.CrimeReportingSystem.entity.Reports;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.repository.KycApplicationRepository;
import com.endava.CrimeReportingSystem.repository.ReportsRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService{

	private final UsersRepository usersRepository;
    private final ReportsRepository reportsRepository;
    private final KycApplicationRepository kycApplicationRepository;

    // Constructor injection
    public DashboardServiceImpl(UsersRepository usersRepository,
                                ReportsRepository reportsRepository,
                                KycApplicationRepository kycApplicationRepository) {
        this.usersRepository = usersRepository;
        this.reportsRepository = reportsRepository;
        this.kycApplicationRepository = kycApplicationRepository;
    }
    
    /* Method to fetch all data counts for active users, reports, and KYC applications */
    @Override
    public ApiGenericResponse<?> getAllDatCount() {
        ApiGenericResponse<Map<String, Integer>> response = new ApiGenericResponse<>(null, null);

        // Fetching lists for users, reports, and KYC applications
        List<Users> userList = usersRepository.findByUserType(UserType.Informant);
        List<Reports> reportList = reportsRepository.findAll();
        List<KycApplication> kycApplicationList = kycApplicationRepository.findAll();
        
        // Initialize map to store counts for different data categories
        @SuppressWarnings("serial")
		Map<String, Integer> allDataCounts = new HashMap<>() {{
            put("activeUsers", 0);
            put("blockedUser", 0);
            put("newReport", 0);
            put("underInvestigationReport", 0);
            put("removedReport", 0);
            put("resolvedReport", 0);
            put("underVerificationKyc", 0);
            put("verifiedKyc", 0);
            put("rejectedKyc", 0);
        }};
        
        // Loop through users and count active and blocked users
        if (!userList.isEmpty()) {
            userList.forEach((user) -> {
                if (user.getIsActive()) {
                    allDataCounts.put("activeUsers", allDataCounts.get("activeUsers") + 1);
                } else {
                    allDataCounts.put("blockedUser", allDataCounts.get("blockedUser") + 1);
                }
            });
        }

        // Loop through reports and categorize them based on their status
        if (!reportList.isEmpty()) {
        	
            reportList.forEach((reports) -> {
            	
                allDataCounts.put(reports.getReportStatus().toString(), allDataCounts.get(reports.getReportStatus().toString()) + 1);
            });
        }

        // Loop through KYC applications and count based on their verification status
        if (!kycApplicationList.isEmpty()) {
            kycApplicationList.forEach((kycapplications) -> {
                allDataCounts.put(kycapplications.getKycVerificationStatus() + "Kyc", allDataCounts.get(kycapplications.getKycVerificationStatus() + "Kyc") + 1);
            });
        }

       
        response.setData(allDataCounts);
        
        return response;
    }

    /* Method to fetch report counts per month for a given year */
    @Override
    public ApiGenericResponse<?> getAllReportByMonth(int year) {
        List<Reports> reportList = reportsRepository.findAll();
       var response = new ApiGenericResponse<>(null, null);

        // Filtering reports for the given year
        List<Reports> filteredReports = reportList.stream()
            .filter(report -> report.getCreatedAt().getYear() == year).toList();
           

        // Grouping reports by month and counting occurrences
        Map<String, Long> monthlyReportCounts = filteredReports.stream()
            .collect(Collectors.groupingBy(
                report -> report.getCreatedAt().getMonth().toString(), 
                Collectors.counting()
            ));

        // Creating a map with all months initialized to 0
        Map<String, Long> allMonths = new LinkedHashMap<>();
       
        for (Month month : Month.values()) {
        	
            allMonths.put(month.toString(), 0L);
        }

        // Update the map with the actual counts for each month
        monthlyReportCounts.forEach(allMonths::put);

        // Handling case when no reports are found for the selected year
        if (allMonths.isEmpty()) {
            response.setMessage(DashboardConstants.ERROR_NO_REPORTS_AVAILABLE.getMessage());
        }

        response.setData(allMonths);
        
        return response;
    }
}
